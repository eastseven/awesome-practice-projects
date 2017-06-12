package cn.eastseven.rbac.web;

import cn.eastseven.rbac.model.User;
import cn.eastseven.rbac.repository.UserRepository;
import cn.eastseven.rbac.service.UserService;
import cn.eastseven.rbac.web.dto.RegisterUser;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * Created by dongqi on 17/5/23.
 */
@Slf4j
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(RegisterUser user) {
        log.debug("{}", user);
        userService.register(user);
        return "login";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @PostMapping("/user/add")
    @ResponseBody
    public Object add() {
        Map<String, Object> response = Maps.newHashMap();
        response.put("msg", "user add success");
        response.put("success", true);
        return response;
    }

    @PostMapping("/user/delete")
    @ResponseBody
    public Object delete() {
        Map<String, Object> response = Maps.newHashMap();
        response.put("msg", "user remove success");
        response.put("success", true);
        return response;
    }

    @GetMapping("/user/modify/{id}")
    public String modify(@PathVariable long id, Model model) {
        model.addAttribute("user", userRepository.findOne(id));
        return "user-edit";
    }

    @PostMapping("/user/modify")
    @ResponseBody
    public Object modify(@RequestParam String fullName, @RequestParam String username) {
        log.debug("{}, {}", fullName, username);
        Map<String, Object> response = Maps.newHashMap();
        response.put("msg", "user modify success");
        response.put("success", true);
        return response;
    }

    @JsonView(DataTablesOutput.View.class)
    @PostMapping("/user/view")
    @ResponseBody
    public DataTablesOutput<User> getUsers(@Valid @RequestBody DataTablesInput input) {
        return userRepository.findAll(input);
    }
}
