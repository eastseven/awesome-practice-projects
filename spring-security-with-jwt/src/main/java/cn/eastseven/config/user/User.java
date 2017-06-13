package cn.eastseven.config.user;

import com.google.common.collect.Lists;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by dongqi on 17/6/13.
 */
@Data
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate = new Date();

    @Transient
    private List<String> roles = Lists.newArrayList("ROLE_USER");
}
