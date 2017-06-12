package cn.eastseven;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SpringBootApplication
public class VueElementUiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueElementUiApplication.class, args);
    }

    @GetMapping("getGroup")
    public Group getGroup() {
        Group group = new Group();
        group.setId(0L);
        group.setName("admin");

        User guestUser = new User();
        guestUser.setId(2L);
        guestUser.setName("guest");

        User rootUser = new User();
        rootUser.setId(3L);
        rootUser.setName("root");

        group.addUser(guestUser);
        group.addUser(rootUser);

        //String jsonString = JSON.toJSONString(group);

        return group;
    }

    @Data
    class User {
        private long id;
        private String name;

    }

    @Data
    class Group {
        private long id;
        private String name;
        private List<User> users = Lists.newArrayList();

        public void addUser(User user) {
            getUsers().add(user);
        }
    }
}
