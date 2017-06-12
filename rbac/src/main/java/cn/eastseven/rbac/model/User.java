package cn.eastseven.rbac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;
import java.util.List;

/**
 * Created by dongqi on 17/5/23.
 */
@Data
@Entity(name = "t_user")
public class User {

    @Id
    @JsonView(DataTablesOutput.View.class)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView(DataTablesOutput.View.class)
    private String username;
    private String password;
    @JsonView(DataTablesOutput.View.class)
    private String fullName;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "t_user_role", joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = Lists.newArrayList();
}
