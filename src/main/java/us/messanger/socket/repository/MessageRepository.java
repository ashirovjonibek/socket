package us.messanger.socket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import us.messanger.socket.entity.Message;
import us.messanger.socket.entity.User;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {

    @Query(value = "select * from message where groups_id=:id",nativeQuery = true)
    List<Message> messageList(Integer id);

    List<Message> findByFromAndTo(User from,User to);
}
