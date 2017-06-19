package cn.eastseven.resource;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by dongqi on 17/6/18.
 */
@Slf4j
@RestController
public class LoginResource {

    @PostMapping(value = "login",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Object login(@RequestBody Map<String, Object> form) {
        Map<String, Object> response = Maps.newHashMap();
        BeanUtils.copyProperties(response, form);
        log.debug("{}", form);

        RestTemplate restTemplate = new RestTemplate();

        String uri = "http://localhost:7071/oauth/token?grant_type=password&username="+form.get("username")+"&password="+form.get("password");
        log.debug("{}", uri);
        HttpHeaders headers = new HttpHeaders();

        String plainClientCredentials = "d7-client-id:123456";
        String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
        headers.add("Authorization", "Basic " + base64ClientCredentials);

        HttpEntity<String> formEntity = new HttpEntity(headers);
        ResponseEntity result = restTemplate.postForEntity(uri, formEntity, Map.class);
        log.debug("{}", result);

        if (result.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = (Map<String, Object>) result.getBody();
            Object accessToken = body.get("access_token");
            Object expires = body.get("expires_in");
            DateTime expiresDateTime = DateTime.now().plusSeconds(Integer.valueOf(expires.toString()));
            log.debug("{}, {}", accessToken, expiresDateTime.toString("yyyy-MM-dd HH:mm:ss"));
            response.put("access_token", accessToken);
            response.put("exp", expiresDateTime.toDate().getTime());
            response.put("username", "d7");

            Map<String, Object> header = Maps.newHashMap();
            header.put("typ", "JWT");
            String jwt = Jwts.builder()
                    .setClaims(response)
                    .setHeader(header)
                    .signWith(SignatureAlgorithm.HS256, "123456".getBytes())
                    .compact();

            Map<String, String> resp = Maps.newHashMap();
            resp.put("token", jwt);
            return resp;
        }

        return response;
    }
}
