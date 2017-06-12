package cn.eastseven.config;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

import static cn.eastseven.config.ClientDetail.KEY_APP_CLIENT;

/**
 * Created by dongqi on 17/6/8.
 */
@Slf4j
@Component
public class OAuth2RequestFactoryImpl extends DefaultOAuth2RequestFactory {

    public OAuth2RequestFactoryImpl(ClientDetailsService clientDetailsService) {
        super(clientDetailsService);
    }

    @Override
    public OAuth2Request createOAuth2Request(ClientDetails client, TokenRequest tokenRequest) {
        OAuth2Request request = super.createOAuth2Request(client, tokenRequest);
        Map<String, Serializable> info = Maps.newHashMap();
        info.put(KEY_APP_CLIENT, (AppClient) client.getAdditionalInformation().get(KEY_APP_CLIENT));

        OAuth2Request oAuth2Request = new OAuth2Request(
                request.getRequestParameters(),
                request.getClientId(),
                request.getAuthorities(),
                request.isApproved(),
                request.getScope(),
                request.getResourceIds(),
                request.getRedirectUri(),
                request.getResponseTypes(),
                info);
        return oAuth2Request;
    }
}
