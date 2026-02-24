package com.gestionPressing.demo.infrastructure.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

class KeycloakJwtAuthenticationConverter extends JwtAuthenticationConverter {

    @Override
    Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        def roles = []

        // Vérifie s'il y a un claim "roles" dans "realm"
        def realm = jwt.getClaimAsMap("roles") ?: jwt.getClaims().get("roles")
        if (!realm) {
            // si le JWT contient "realm_access" (structure standard Keycloak)
            def realmAccess = jwt.getClaims().get("realm_access")
            if (realmAccess && realmAccess["roles"]) {
                roles = realmAccess["roles"]
            }
        } else {
            roles = realm
        }

        // Transforme les rôles en authorities Spring
        roles.collect { roleName ->
            // s'assure que c'est bien préfixé ROLE_ si nécessaire
            roleName.startsWith("ROLE_") ? new SimpleGrantedAuthority(roleName) :
                    new SimpleGrantedAuthority("ROLE_${roleName}")
        }
    }
}