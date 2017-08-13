package cn.eastseven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by dongqi on 17/7/18.
 */
@Slf4j
@Controller
public class FormController {

    @GetMapping(value = {"/form.html", "/index.html", "/", "/form"})
    public ModelAndView form() {
        return new ModelAndView("form", "formObject", new FormObject());
    }

    @PostMapping(value = {"/form.html", "/form"})
    public String submit(HttpServletRequest request, @Valid FormObject form,
                         BindingResult bindingResult,
                         @RequestParam("file1") MultipartFile file1,
                         @RequestParam("file2") MultipartFile file2) {
        log.debug("{}, {}", request, form);
        log.debug("file1={}, file2={}", file1.isEmpty(), file2.isEmpty());

        if (bindingResult.hasErrors()) {
            return "form";
        }

        return "redirect:/success.html";
    }

    @GetMapping("/success.html")
    public String success() {
        return "success";
    }
}
