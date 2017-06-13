package cn.eastseven.config.user;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by dongqi on 17/6/13.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(final String username);
}
