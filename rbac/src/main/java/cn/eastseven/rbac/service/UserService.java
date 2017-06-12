package cn.eastseven.rbac.service;

import cn.eastseven.rbac.model.Permission;
import cn.eastseven.rbac.model.Role;
import cn.eastseven.rbac.model.User;
import cn.eastseven.rbac.repository.RoleRepository;
import cn.eastseven.rbac.repository.UserRepository;
import cn.eastseven.rbac.web.dto.RegisterUser;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dongqi on 17/5/23.
 */
@Slf4j
@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("{}", username);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
            List<Role> roles = user.getRoles();
            for (Role role : roles) {
                List<Permission> permissions = role.getPermissions();//permissionDao.findByAdminUserId(user.getId());
                for (Permission permission : permissions) {
                    if (permission != null && permission.getName() != null) {

                        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
                        //1：此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
                        grantedAuthorities.add(grantedAuthority);
                    }
                }
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
            }

        }

        throw new UsernameNotFoundException("admin: " + username + " do not exist!");
    }

    public void register(RegisterUser registerUser) {

        User user = new User();
        user.setUsername(registerUser.getEmail());
        user.setFullName(registerUser.getUsername());
        user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        user.setRoles(Lists.newArrayList(roleRepository.findByName("ROLE_NORMAL_USER")));

        userRepository.save(user);
        log.debug("{}", user);
    }
}
