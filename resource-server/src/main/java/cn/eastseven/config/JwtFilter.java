package cn.eastseven.config;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * Created by dongqi on 17/6/16.
 */
@Slf4j
@Component
public class JwtFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug(" ----- JWT Filter ----- ");

        HttpServletRequest req = (HttpServletRequest) request;

        String uri = req.getRequestURI();
        if (uri.contains("login")) {
            chain.doFilter(request, response);
        }

        String value = req.getHeader("Authorization");
        if (StringUtils.isNotBlank(value)) {
            String jwtString = value;// value.split(" ")[1];

            JwtParser jwtParser = Jwts.parser().setSigningKey("123456".getBytes());
            Jwt jwt = jwtParser.parse(jwtString);
            Object body = jwt.getBody();

            log.debug("{}", jwt);

            DefaultClaims claims = (DefaultClaims) body;
            Object token = claims.get("access_token");
            Request wrapper = new Request(req);
            wrapper.addHeader("Authorization", "Bearer " + token);
            chain.doFilter(wrapper, response);
        } else {
            // chain.doFilter(request, response);
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(SC_UNAUTHORIZED);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.sendError(SC_UNAUTHORIZED, "没有权限");
        }

    }

    @Override
    public void destroy() {

    }

    class Request extends HttpServletRequestWrapper {

        private Map<String, String> headers;

        public Request(HttpServletRequest request) {
            super(request);

            this.headers = Maps.newHashMap();

            Enumeration<String> enumeration = request.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement();
                headers.put(name, request.getHeader(name));
            }
        }

        @Override
        public String getHeader(String name) {
            return this.headers.get(name);
        }

        public void addHeader(String name, String value) {
            this.headers.put(name, value);
        }
    }
}
