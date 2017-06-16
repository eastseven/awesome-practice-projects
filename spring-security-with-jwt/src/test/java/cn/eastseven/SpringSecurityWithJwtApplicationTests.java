package cn.eastseven;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringSecurityWithJwtApplicationTests {

	/*
    *
	* POST {{localhost}}:7071/oauth/token?grant_type=password&username=admin&password=123456
	*
	* {
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsImV4dF9kYXRhIjp7ImlkIjoxNDk3NDA5MTI2ODAyLCJuYW1lIjoibXktYXBwIiwiZnVsbE5hbWUiOiLmtYvor5VBUFAiLCJjcmVhdGUiOjE0OTc0MDkxMjY4MDJ9LCJzY29wZSI6WyJ0cnVzdCJdLCJleHAiOjE0OTc0NTIzMjYsImF1dGhvcml0aWVzIjpbIkFETUlOIl0sImp0aSI6ImI0OTQ5NGY1LWYzOTUtNDFiNi05ZTE1LWEzMjE3MDE3NjEyMiIsImNsaWVudF9pZCI6ImQ3LWNsaWVudC1pZCJ9.EwAljECslUl12Q2h6QI9reRPDVjON5osJ7gnCV6VptxVNAqEbDphjLQnLyAAEkhC2_dwGhs7vq9XIziGFjKVo1eXWek9Z7AWkCK0CNAHFw0KfvD9isiup1poUEw2jpYksoHwGkSOa9WfbVb1HhxQuTaCiaLrMTRL-vDiAn6nRwibNgK4Bv272Ux2NwkOf6ltp-F32vAnvTg_pKys3TpS2r7dbvNu0bj1OZ_sRx6YgsOzQz2MeX3b_zBCo470l9pwP9bJOBEWRvUNed5X8jLDZTCfEgnP_MkeC-18fSS1NNZp68_n9Yy0CJJwPhyCTOzxhQwQ8TSYL64XUQdwXAc5Eg",
    "token_type": "bearer",
    "expires_in": 43199,
    "scope": "trust",
    "ext_data": {
        "id": 1497409126802,
        "name": "my-app",
        "fullName": "测试APP",
        "create": 1497409126802
    },
    "jti": "b49494f5-f395-41b6-9e15-a32170176122"
}
	* */

    @Test
    public void contextLoads() {
        final String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsImV4dF9kYXRhIjp7ImlkIjoxNDk3NDA5MTI2ODAyLCJuYW1lIjoibXktYXBwIiwiZnVsbE5hbWUiOiLmtYvor5VBUFAiLCJjcmVhdGUiOjE0OTc0MDkxMjY4MDJ9LCJzY29wZSI6WyJ0cnVzdCJdLCJleHAiOjE0OTc0NTIzMjYsImF1dGhvcml0aWVzIjpbIkFETUlOIl0sImp0aSI6ImI0OTQ5NGY1LWYzOTUtNDFiNi05ZTE1LWEzMjE3MDE3NjEyMiIsImNsaWVudF9pZCI6ImQ3LWNsaWVudC1pZCJ9.EwAljECslUl12Q2h6QI9reRPDVjON5osJ7gnCV6VptxVNAqEbDphjLQnLyAAEkhC2_dwGhs7vq9XIziGFjKVo1eXWek9Z7AWkCK0CNAHFw0KfvD9isiup1poUEw2jpYksoHwGkSOa9WfbVb1HhxQuTaCiaLrMTRL-vDiAn6nRwibNgK4Bv272Ux2NwkOf6ltp-F32vAnvTg_pKys3TpS2r7dbvNu0bj1OZ_sRx6YgsOzQz2MeX3b_zBCo470l9pwP9bJOBEWRvUNed5X8jLDZTCfEgnP_MkeC-18fSS1NNZp68_n9Yy0CJJwPhyCTOzxhQwQ8TSYL64XUQdwXAc5Eg";
        final String result = Base64.getEncoder().encodeToString(token.getBytes());
        log.debug("before\t{}, {}", token.length(), token);
        log.debug("after\t{}, {}", result.length(), result);
    }

}
