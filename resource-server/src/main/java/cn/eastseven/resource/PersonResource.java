package cn.eastseven.resource;

import cn.eastseven.module.human.Person;
import cn.eastseven.module.human.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dongqi on 17/6/8.
 */
@Slf4j
@RestController
@RequestMapping(value = "persons", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonResource {

    @Autowired
    PersonRepository personRepository;

    @GetMapping("/{id}")
    public Object getFullName(@PathVariable long id,
                              Principal principal,
                              @RequestHeader("Authorization") String authorization) {

        log.debug("{}, {}", principal.getName(), authorization);

        Jwt jwt = JwtHelper.decode(authorization.split(" ")[1]);
        log.debug("{}, {}", jwt.getClaims(), jwt.getEncoded());

        Map<String, Object> response = new HashMap();
        response.put("fullname", "无名氏");

        if (personRepository.exists(id)) {
            Person person = personRepository.findOne(id);
            response.put("fullname", person.getFirstName() + person.getLastName());
        }

        return response;
    }
}
