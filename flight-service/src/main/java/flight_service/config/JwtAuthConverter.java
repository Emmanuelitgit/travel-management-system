package flight_service.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt,AbstractAuthenticationToken> {

    /**
     * This is usd to concatenate the extracted realm and resource roles as one authorities set.
     * @param jwt
     * @return JwtAuthenticationToken
     * @auther Emmanuel Yidana
     * @createdAt 16h April 2025
     */
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                extractRealmRoles(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        String principal = jwt.getClaimAsString("preferred_username");
        return new JwtAuthenticationToken(jwt, authorities, principal);
    }

    /**
     * This is used to extract realm roles from the keycloak token.
     * @param jwt
     * @return JwtAuthenticationToken
     * @auther Emmanuel Yidana
     * @createdAt 16h April 2025
     */
    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        Collection<String> roles = (Collection<String>) realmAccess.get("roles");
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    /**
     * extracting resource roles from the keycloak token
     * @param jwt
     * @return JwtAuthenticationToken
     * @auther Emmanuel Yidana
     * @createdAt 16h April 2025
     */
    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null || !resourceAccess.containsKey("realm-management")) {
            return Collections.emptyList();
        }

        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get("realm-management");
        if (resource == null || !resource.containsKey("roles")) {
            return Collections.emptyList();
        }

        Collection<String> roles = (Collection<String>) resource.get("roles");
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}