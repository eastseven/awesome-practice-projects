package cn.eastseven.rbac.web.dto;

import lombok.Data;

/**
 * Created by dongqi on 17/5/23.
 */
@Data
public class RegisterUser {

    private String username;
    private String password;
    private String email;

}
