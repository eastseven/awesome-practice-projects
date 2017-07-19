package cn.eastseven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dongqi on 17/7/18.
 */
@Slf4j
@Controller
public class FormController {

    @GetMapping(value = {"/form.html", "/index.html", "/"})
    public String form() {
        return "form";
    }

    @PostMapping("/form/submit")
    public String submit(HttpServletRequest request, FormObject form) {
        log.debug("{}, {}", request, form);
        return "redirect:/success.html";
    }

    @GetMapping("/success.html")
    public String success() {
        return "success";
    }
}
