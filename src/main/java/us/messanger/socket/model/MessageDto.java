package us.messanger.socket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.messanger.socket.entity.MessageType;
import us.messanger.socket.entity.User;

import javax.persistence.OneToOne;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String message;

    private String createdAt;

    private MessageType type;

    private Integer groupId;

    private Integer fromId;

    private Integer toId;
}
