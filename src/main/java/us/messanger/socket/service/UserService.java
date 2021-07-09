package us.messanger.socket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.messanger.socket.entity.User;
import us.messanger.socket.model.ApiResponse;
import us.messanger.socket.repository.ChatRepository;
import us.messanger.socket.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRepository chatRepository;

    public ApiResponse saveUser(User user){
        ApiResponse user1 = getUser(user.getUsername(), user.getName());
        User user2=new User();
       if(!user1.isSuccess()) {
           user2=userRepository.save(user);
       }
        return new ApiResponse(true,user1.isSuccess()?user1.getUserList():user2);
    }

    public ApiResponse getUser(String username, String name){
        User user=userRepository.findByUsernameAndName(username,name);
        if (user!=null){
            return new ApiResponse(true,user);
        }
        return new ApiResponse(false,null);
    }

    public User findById(Integer id){
        return userRepository.findById(id).orElseThrow(()->new IllegalStateException("user not found"));
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
}
