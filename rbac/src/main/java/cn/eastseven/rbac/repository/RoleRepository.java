package cn.eastseven.rbac.repository;

import cn.eastseven.rbac.model.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by dongqi on 17/5/23.
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);
}
