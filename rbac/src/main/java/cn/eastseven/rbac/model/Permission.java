package cn.eastseven.rbac.model;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Created by dongqi on 17/5/23.
 */
@Data
@ToString(exclude = {"roles"})
@Entity(name = "t_permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private String url;
    private Long pid;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles = Lists.newArrayList();
}
