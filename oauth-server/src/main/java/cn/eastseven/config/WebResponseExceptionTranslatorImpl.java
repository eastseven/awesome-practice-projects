package cn.eastseven.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * Created by dongqi on 17/6/9.
 */
@Slf4j
@Component
public class WebResponseExceptionTranslatorImpl implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.debug(" ----- {} -----", e.getMessage());
        return ResponseEntity.ok(new OAuth2Exception("报错了"));
    }
}
