package cn.eastseven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class AjaxFormController {

    @GetMapping("/form-ajax.html")
    public ModelAndView form() {
        return new ModelAndView("form-ajax", "formObject", new FormObject());
    }

    @PostMapping(value = "/form/ajax/submit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object submit(@Valid @RequestBody FormObject formObject, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap();

        log.debug("{}", formObject);
        log.debug("error: {}", bindingResult);

        if (bindingResult.hasErrors()) {
            response.put("success", false);
            response.put("error", bindingResult.getAllErrors());
            return response;
        }

        response.put("success", true);
        return response;
    }
}
