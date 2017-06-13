package cn.eastseven.config.auth;

import cn.eastseven.config.user.User;

/**
 * Created by dongqi on 17/6/13.
 */
public interface AuthService {

    User register(User userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}
