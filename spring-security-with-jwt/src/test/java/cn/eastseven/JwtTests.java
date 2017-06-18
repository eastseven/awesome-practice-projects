package cn.eastseven;

import cn.eastseven.config.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * Created by dongqi on 17/6/18.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTests {

    @Test
    public void testCreateJwt() throws JsonProcessingException {
        Map<String, Object> header = Maps.newHashMap();
        header.put("typ", "JWT");

        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setEmail("debug7@yeah.net");
        user.setUsername("debug7");
        user.setPassword("123456");
        Map<String, Object> claim = Maps.newHashMap();
        claim.put("user", user);

        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(user);
        log.info("{}", userJson);
        String jwt = Jwts.builder()
                .setExpiration(DateTime.now().plusMinutes(5).toDate())
                .setAudience("aud")
                .setId("jti")
                .setIssuedAt(DateTime.now().toDate())
                .setIssuer("iss")
                .setNotBefore(DateTime.now().toDate())
                .setSubject("sub")
                //.setClaims(claim)
                .setHeader(header)
                //.setPayload(userJson)
                .signWith(SignatureAlgorithm.HS256, "123456".getBytes())
                .compact()
        ;

        Assert.assertNotNull(jwt);
        log.info("{}", jwt);
    }
}
