package us.messanger.socket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import us.messanger.socket.entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsernameAndName(String username, String name);
    boolean existsByUsername(String username);
}
