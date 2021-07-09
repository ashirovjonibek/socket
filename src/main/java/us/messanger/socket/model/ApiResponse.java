package us.messanger.socket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private boolean success;

    private Object userList;

    private Object groups;

    private Object messages;

    public ApiResponse(boolean success, Object userList) {
        this.success = success;
        this.userList = userList;
    }

    public ApiResponse(boolean success, Object userList, Object groups) {
        this.success = success;
        this.userList = userList;
        this.groups = groups;
    }


}
