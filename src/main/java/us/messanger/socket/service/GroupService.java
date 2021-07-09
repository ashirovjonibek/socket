package us.messanger.socket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.messanger.socket.entity.Groups;
import us.messanger.socket.repository.GroupRepository;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRepository;

    public Groups save(Groups group){
        return groupRepository.save(group);
    }

    public List<Groups> groupsList(){
        return groupRepository.findAll();
    }

    public boolean isGroup(String username){
        return groupRepository.existsByGroupUsername(username);
    }
}
