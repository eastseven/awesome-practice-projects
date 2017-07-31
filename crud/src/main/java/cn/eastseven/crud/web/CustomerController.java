package cn.eastseven.crud.web;

import cn.eastseven.crud.model.Customer;
import cn.eastseven.crud.model.CustomerForm;
import cn.eastseven.crud.service.CustomerService;
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

@Slf4j
@Controller
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @JsonView(DataTablesOutput.View.class)
    @PostMapping("/data/customers")@ResponseBody
    public DataTablesOutput<Customer> retrieve(@Valid @RequestBody DataTablesInput input) {
        return customerService.retrieve(input);
    }

    @GetMapping("/customer/add")
    public String add(Model model) {
        model.addAttribute("title", "新增");
        return "customer-form";
    }

    @PostMapping("/customer/add")@ResponseBody
    public Object add(CustomerForm form) {
        log.debug("add {}", form);
        Map<String, Object> response = Maps.newHashMap();
        try {
            customerService.create(form);
            response.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
        }
        return response;
    }

    @GetMapping("/customer/view/{id}")
    public String view(@PathVariable long id, Model model) {
        model.addAttribute("customer", customerService.retrieve(id));
        model.addAttribute("title", "查看");
        return "customer-form";
    }

    @GetMapping("/customer/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        model.addAttribute("customer", customerService.retrieve(id));
        model.addAttribute("title", "修改");
        return "customer-form";
    }

    @PostMapping("/customer/edit")@ResponseBody
    public Object edit(CustomerForm form) {
        log.debug("edit {}", form);
        Map<String, Object> response = Maps.newHashMap();
        try {
            customerService.update(form);
            response.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
        }
        return response;
    }

    @GetMapping("/customer/delete/{id}")@ResponseBody
    public Object del(@PathVariable long id) {
        Map<String, Object> response = Maps.newHashMap();

        try {
            customerService.delete(id);
            response.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
        }

        return response;
    }

    @PostMapping("/validate/customer")
    @ResponseBody
    public Object validate(@RequestParam String field, @RequestParam String value, @RequestParam String origin) {
        log.debug("field={}, value={}, origin={}", field, value, origin);
        return true;
    }
}
