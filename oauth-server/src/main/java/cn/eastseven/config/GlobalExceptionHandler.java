package cn.eastseven.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dongqi on 17/6/9.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            ClientAuthenticationException.class,
            OAuth2AccessDeniedException.class,
            UnsupportedResponseTypeException.class,
            UserDeniedAuthorizationException.class,
            OAuth2Exception.class})
    @ResponseBody
    public Object jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.debug("{}", e.getClass());
        Map<String, Object> r = new HashMap();
        r.put("msg", e.getMessage());
        //r.put("oauth2ErrorCode", e.getOAuth2ErrorCode());
        //r.put("httpErrorCode", e.getHttpErrorCode());
        r.put("url", req.getRequestURL().toString());
        return r;
    }

}
