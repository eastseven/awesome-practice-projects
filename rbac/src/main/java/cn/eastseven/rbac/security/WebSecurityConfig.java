package cn.eastseven.rbac.security;

import cn.eastseven.rbac.repository.UserRepository;
import cn.eastseven.rbac.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dongqi on 17/5/23.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final static String[] ignore =
            {"/static/**", "/webjars/**",
                    "/login", "/register",
                    "/build/**", "/images/**",
                    "/js/**", "/vendors/**", "/4**", "/5**", "/error"};

    @Autowired
    FilterSecurityInterceptorImpl filterSecurityInterceptorImpl;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    LogoutHandlerImpl logoutHandler;

    @Autowired
    AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ignore).permitAll()
                .anyRequest().authenticated() //任何请求,登录后可以访问
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .successHandler(loginSuccessHandler)
                .permitAll() //登录页面用户任意访问
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .clearAuthentication(true)
                .addLogoutHandler(logoutHandler)
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403")
                .accessDeniedHandler(accessDeniedHandler)
            .and()
                .csrf().disable()
        ; //注销行为任意访问

        http.addFilterBefore(filterSecurityInterceptorImpl, FilterSecurityInterceptor.class);
    }

    @Component
    class AccessDeniedHandlerImpl implements AccessDeniedHandler {

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            //log.debug("AccessDeniedHandler\t{}, {}, {}", accessDeniedException, request, response);
            log.debug("{}, {}, {}, {}", request.getContentType(), request.getMethod(), request.getRequestURI(), accessDeniedException.getMessage());
            if ("get".equalsIgnoreCase(request.getMethod())) {
                response.sendRedirect("/403");
            } else {
                response.sendError(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage());
            }
        }
    }

    @Component
    class LoginSuccessHandler implements AuthenticationSuccessHandler {

        @Autowired
        UserRepository userRepository;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            log.debug("LoginSuccessHandler\t{}, {}, {}", authentication.getPrincipal(), request, response);

            User user = (User) authentication.getPrincipal();
            request.getSession().setAttribute("displayName", userRepository.findByUsername(user.getUsername()).getFullName());
            //跳转页面
            response.sendRedirect("/dashboard");
        }
    }

    @Component
    class LogoutHandlerImpl implements LogoutHandler {

        @Override
        public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
            log.debug("LogoutHandler\t{}, {}, {}", authentication.getPrincipal(), request, response);
        }
    }
}
