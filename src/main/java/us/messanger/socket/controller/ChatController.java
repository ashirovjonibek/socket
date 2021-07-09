package us.messanger.socket.controller;

import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import us.messanger.socket.config.WebSocketEventListener;
import us.messanger.socket.entity.*;
import us.messanger.socket.model.ApiResponse;
import us.messanger.socket.model.MessageDto;
import us.messanger.socket.repository.ChatRepository;
import us.messanger.socket.service.GroupService;
import us.messanger.socket.service.MessageService;
import us.messanger.socket.service.UserService;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    GroupService groupService;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @GetMapping("/")
    public String main(){
        return "main";
    }

    @MessageMapping("/chat/{chatId}/sendMessage")
    public void sendMessage(@DestinationVariable String chatId, @Payload MessageDto dto) {
        if (!dto.getType().equals(MessageType.JOIN)){
            messageService.save(dto);
        }
        if (dto.getGroupId()==null){
            List<Message> fromUserChats = messageService.getFromUserChats(dto.getFromId(), dto.getToId());
            List<Message> fromUserChats1 = messageService.getFromUserChats(dto.getToId(), dto.getFromId());
            fromUserChats1.forEach(f->{
                fromUserChats.add(f);
            });
            List<Message> messages = sortMessage(fromUserChats);
            messagingTemplate.convertAndSend(format("/channel/%s", chatId), new ApiResponse(true,null,null,messages));
        }else {
            List<Message> messages = messageService.getMessageFromGroupId(dto.getGroupId());
            messagingTemplate.convertAndSend(format("/channel/%s", chatId), new ApiResponse(true,null,null,messages));

        }
    }

    @MessageMapping("/chat/{roomId}")
    public void addUser(@DestinationVariable String roomId, @Payload MessageDto message,
                        SimpMessageHeaderAccessor headerAccessor) {

        if (!message.getType().equals(MessageType.JOIN)){
            messageService.save(message);
        }
       if (message.getGroupId()==null){
           User save = userService.findById(message.getFromId());
           List<Message> fromUserChats = messageService.getFromUserChats(message.getFromId(), message.getToId());
           List<Message> fromUserChats1 = messageService.getFromUserChats(message.getToId(), message.getFromId());
           fromUserChats1.forEach(f->{
               fromUserChats.add(f);
           });
           List<Message> messages = sortMessage(fromUserChats);
           headerAccessor.getSessionAttributes().put("username", save.getUsername());
           messagingTemplate.convertAndSend(format("/channel/%s", roomId), new ApiResponse(true,null,null,messages));
       }
       else {
           messageService.save(message);
           messagingTemplate.convertAndSend(format("/channel/%s", roomId), new ApiResponse(true,null,null,messageService.getMessageFromGroupId(message.getGroupId())));
       }
    }

    @MessageMapping("/chat/home/addUser")
    public void userHome(@Payload User user,
                        SimpMessageHeaderAccessor headerAccessor) {
        User response = (User) userService.saveUser(user).getUserList();
        List<User> all = userService.findAll();
        all.forEach(user1 -> {
            if (response.getId()!=user1.getId()){
                Chat chat=chatRepository.findByUsername1AndUsername2(response.getUsername(),user1.getUsername());
                if (chat==null){
                    chat=chatRepository.findByUsername1AndUsername2(user1.getUsername(),response.getUsername());
                    if (chat==null){
                        chat=new Chat();
                        chat.setName(response.getUsername()+user1.getUsername());
                        chat.setUsername1(response.getUsername());
                        chat.setUsername2(user1.getUsername());
                        chatRepository.save(chat);
                    }
                }

            }
        });
        List<Groups> groups = groupService.groupsList();
        ApiResponse response1=new ApiResponse(true,all,groups);
        headerAccessor.getSessionAttributes().put("username", response.getUsername());
        messagingTemplate.convertAndSend("/channel/home", response1);
    }

    @MessageMapping("chat/home/addGroup")
    public void addGroup(@Payload Groups gr){
        groupService.save(gr);
        List<Groups> groups = groupService.groupsList();
        messagingTemplate.convertAndSend("/channel/home",new ApiResponse(true,userService.findAll(), groups,null));
    }


    private List<Message> sortMessage(List<Message> messages){
        Message message;
        for (int i = 0; i < messages.size(); i++) {
            for (int j = 0; j < messages.size() - 1; j++) {
                if (messages.get(j).getId()>messages.get(j+1).getId()){
                   message= messages.get(j+1);
                   messages.set(j+1,messages.get(j));
                   messages.set(j,message);
                }
            }
        }

        return messages;
    }
}
