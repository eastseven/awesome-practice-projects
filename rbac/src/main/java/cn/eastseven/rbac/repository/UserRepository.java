package cn.eastseven.rbac.repository;

import cn.eastseven.rbac.model.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Created by dongqi on 17/5/23.
 */
public interface UserRepository extends DataTablesRepository<User, Long> {

    User findByUsername(String username);
}
