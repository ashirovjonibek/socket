package us.messanger.socket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.messanger.socket.entity.Chat;
import us.messanger.socket.repository.ChatRepository;
import us.messanger.socket.repository.UserRepository;
import us.messanger.socket.service.GroupService;

@RestController
@RequestMapping("api/chanel")
public class GetChanelName {
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupService groupService;

    @GetMapping("/name")
    public HttpEntity<?> getName(String username1, String username2){
        Chat chat = chatRepository.findByUsername1AndUsername2(username1, username2);
        if (chat==null){
            chat=chatRepository.findByUsername1AndUsername2(username2,username1);
        }
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/existsByUsername")
    public HttpEntity<?> existsByUsername(@RequestParam String username){
        return ResponseEntity.ok(userRepository.existsByUsername(username));
    }

    @GetMapping("/existsByGroup")
    public HttpEntity<?>existsByGroup(@RequestParam String username){
        return ResponseEntity.ok(groupService.isGroup(username));
    }

}
