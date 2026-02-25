package com.gestionPressing.demo.infrastructure.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.oauth2.jwt.Jwt

class KeycloakJwtAuthenticationConverter extends JwtAuthenticationConverter {

    KeycloakJwtAuthenticationConverter() {
        def converter = new JwtGrantedAuthoritiesConverter()
        // Par défaut, Spring lit "scope", on ne touche pas au scope
        this.setJwtGrantedAuthoritiesConverter { Jwt jwt ->
            def roles = []
            def realmAccess = jwt.claims['realm_access']
            if (realmAccess && realmAccess['roles']) {
                roles = realmAccess['roles']
            }
            roles.collect { roleName ->
                roleName.startsWith("ROLE_") ? new SimpleGrantedAuthority(roleName) :
                        new SimpleGrantedAuthority("ROLE_${roleName}")
            } as Collection<GrantedAuthority>
        }
    }
}