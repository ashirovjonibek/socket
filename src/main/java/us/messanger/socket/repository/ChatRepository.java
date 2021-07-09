package us.messanger.socket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import us.messanger.socket.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat,Integer> {
    Chat findByUsername1AndUsername2(String username1, String username2);
}
