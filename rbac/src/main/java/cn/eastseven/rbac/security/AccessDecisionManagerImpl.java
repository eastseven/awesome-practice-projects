package cn.eastseven.rbac.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by dongqi on 17/5/23.
 */
@Slf4j
@Service
public class AccessDecisionManagerImpl implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        log.debug("\n{}\n{}\n{}", authentication, object, configAttributes);
        if (null == configAttributes || configAttributes.size() <= 0) {
            return;
        }
        ConfigAttribute c;
        String needRole;
        for (Iterator<ConfigAttribute> iterator = configAttributes.iterator(); iterator.hasNext(); ) {
            c = iterator.next();
            needRole = c.getAttribute();
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                log.debug("{} vs {}", needRole, ga.getAuthority());
                if (needRole.trim().equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("no right");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
