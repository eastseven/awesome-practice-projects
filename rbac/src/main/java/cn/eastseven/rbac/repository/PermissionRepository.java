package cn.eastseven.rbac.repository;

import cn.eastseven.rbac.model.Permission;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by dongqi on 17/5/23.
 */
public interface PermissionRepository extends CrudRepository<Permission, Long> {
}
