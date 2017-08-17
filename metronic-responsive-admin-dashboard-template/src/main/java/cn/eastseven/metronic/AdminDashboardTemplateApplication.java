package cn.eastseven.metronic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
@SpringBootApplication
public class AdminDashboardTemplateApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(AdminDashboardTemplateApplication.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/").setViewName("index");
        registry.addViewController("/login.html").setViewName("login");
    }

    @GetMapping(value = {"/", "/index.html"})
    public String index() {
        return "index";
    }
}
