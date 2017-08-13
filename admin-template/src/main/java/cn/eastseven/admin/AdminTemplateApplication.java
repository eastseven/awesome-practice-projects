package cn.eastseven.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class AdminTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminTemplateApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "gentelella/index";
    }

    @GetMapping(value = {"/ace", "/ace/index.html"})
    public String index(Model model) {
        return "ace/index";
    }

    @GetMapping("/ace/hello.html")
    public String hello() {
        return "ace/hello";
    }

}
