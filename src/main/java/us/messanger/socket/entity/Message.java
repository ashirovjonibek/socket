package us.messanger.socket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String message;

    private String createdAt;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @ManyToOne
    private Groups groups;

    @OneToOne
    private User from;
    @OneToOne
    private User to;
}
