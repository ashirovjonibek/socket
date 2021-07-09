package us.messanger.socket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.messanger.socket.entity.Groups;
import us.messanger.socket.entity.Message;
import us.messanger.socket.entity.User;
import us.messanger.socket.model.MessageDto;
import us.messanger.socket.repository.GroupRepository;
import us.messanger.socket.repository.MessageRepository;
import us.messanger.socket.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    GroupRepository groupRepository;

    public Message save(MessageDto message) {
        User fromUser = userRepository.findById(message.getFromId()).orElseThrow(() -> new IllegalStateException("User not found"));
        Message message1=new Message();
        if (message.getGroupId() != null) {
            Groups group=groupRepository.findById(message.getGroupId()).orElseThrow(()->new IllegalStateException("group not found"));
            message1.setGroups(group);
        }
        if (message.getToId()!=null){
            User toUser = userRepository.findById(message.getToId()).orElseThrow(() -> new IllegalStateException("User not found"));
            message1.setTo(toUser);
        }
        message1.setCreatedAt(message.getCreatedAt());
        message1.setFrom(fromUser);
        message1.setType(message.getType());
        message1.setMessage(message.getMessage());
        return messageRepository.save(message1);
    }
    
    public List<Message> getFromUserChats(Integer from, Integer to){
        return messageRepository.findByFromAndTo(
                userRepository.findById(from).get(),
                userRepository.findById(to).get());
    }

    public List<Message> getMessageFromGroupId(Integer groupId){
        return messageRepository.messageList(groupId);
    }
}
