package cn.eastseven.rbac.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by dongqi on 17/5/23.
 */
@Slf4j
@Controller
@RequestMapping("dashboard")
public class DashboardController {

    @GetMapping
    public String dashboard(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("{}, {}", user.getUsername(), user.getAuthorities());
        model.addAttribute("username", user.getUsername());
        return "dashboard";
    }
}
