package cn.eastseven.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class AdminTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminTemplateApplication.class, args);
	}

	@GetMapping("/")
	public String index() {
		return "gentelella/index";
	}
}
