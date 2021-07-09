package us.messanger.socket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import us.messanger.socket.entity.Groups;

public interface GroupRepository extends JpaRepository<Groups,Integer> {
    boolean existsByGroupUsername(String username);
}
