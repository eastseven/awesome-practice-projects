package cn.eastseven.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by dongqi on 17/5/26.
 */
public class ClientDetail implements ClientDetails {

    public static final String KEY_APP_CLIENT = "ext_data";

    private AppClient client;

    public ClientDetail(AppClient client) {
        this.client = client;
    }

    @Override
    public String getClientId() {
        return "d7-client-id";
    }

    @Override
    public Set<String> getResourceIds() {
        return null;
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return "123456";
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return Sets.newHashSet("trust");
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return Sets.newHashSet("client_credentials", "password", "authorization_code");
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return Sets.newHashSet("https://www.getpostman.com/oauth2/callback");
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Lists.newArrayList(new SimpleGrantedAuthority("r"), new SimpleGrantedAuthority("w"));
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return 180;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return null;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        Map<String, Object> info = Maps.newHashMap();
        info.put(KEY_APP_CLIENT, this.client);
        return info;
    }
}
