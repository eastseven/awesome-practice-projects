package cn.eastseven.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dongqi on 17/5/23.
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class OAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ClientDetailsServiceImpl clientDetailsService;

    @Value("${keystore.password}")
    String password;

    @Bean
    protected JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    protected JwtAccessTokenConverter jwtAccessTokenConverter() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("jwt.jks"), password.toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
        return converter;
    }

    @Bean
    protected OAuth2RequestFactory requestFactory() {
        return new OAuth2RequestFactoryImpl(clientDetailsService);
    }

    @Bean
    @Primary
    protected TokenServiceImpl tokenServices() {
        return new TokenServiceImpl(jwtTokenStore(), jwtAccessTokenConverter());
    }

    @Autowired WebResponseExceptionTranslatorImpl webResponseExceptionTranslator;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
        /*
        clients.inMemory()
                .withClient("d7")
                .secret("123456")
                .resourceIds("11010111")
                .scopes("trust")
                .authorizedGrantTypes("client_credentials", "password")
                .authorities("r", "w")
                .redirectUris("https://www.getpostman.com/oauth2/callback")
            .and()
                .withClient("dq")
                .secret("123456")
                .resourceIds("11010111")
                .scopes("trust")
                .authorizedGrantTypes("authorization_code").autoApprove(true)
                .authorities("r", "w")
                .redirectUris("https://www.getpostman.com/oauth2/callback")*/
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        log.debug("\n\n{}, {}", request.getRequestURL(), authException);
                        //response.addHeader("WWW-Authenticate", "Basic realm=\"\"");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                    }
                })
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                //.exceptionTranslator(webResponseExceptionTranslator)
                .tokenServices(tokenServices())
                .accessTokenConverter(jwtAccessTokenConverter())
                .requestFactory(requestFactory())
                //.getFrameworkEndpointHandlerMapping().
        ;
    }

}
