package cn.eastseven;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by dongqi on 17/6/6.
 */
@Slf4j
@RestController
@RequestMapping("/session")
public class SessionShareResource {

    final String p = "yyyy-MM-dd HH:mm:ss";

    @GetMapping
    public Object get(HttpServletRequest request) {
        HttpSession session = request.getSession();
        log.info("{},{},{},{},{}",request.getRemoteHost(),
                session.isNew(), session.getId(),
                new DateTime(session.getCreationTime()).toString(p),
                new DateTime(session.getLastAccessedTime()).toString(p));
        return true;
    }
}
