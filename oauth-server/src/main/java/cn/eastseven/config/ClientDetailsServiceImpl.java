package cn.eastseven.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

/**
 * Created by dongqi on 17/5/26.
 */
@Slf4j
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.debug("{}", clientId);
        AppClient client = new AppClient();
        client.setId(System.currentTimeMillis());
        client.setName("my-app");
        client.setFullName("测试APP");
        return new ClientDetail(client);
    }
}