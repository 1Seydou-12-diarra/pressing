package com.gestionPressing.demo.infrastructure.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

class KeycloakJwtAuthenticationConverter extends JwtAuthenticationConverter {

    KeycloakJwtAuthenticationConverter() {
        setJwtGrantedAuthoritiesConverter(new RealmRoleConverter())
    }

    static class RealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        Collection<GrantedAuthority> convert(Jwt jwt) {
            def realmAccess = jwt.getClaim("realm_access")
            def roles = realmAccess?.get("roles") ?: []

            println("Roles dans le token: $roles")

            roles.collect { role ->
                def cleanRole = role.toString().trim()
                def finalRole = cleanRole.startsWith("ROLE_") ? cleanRole : "ROLE_$cleanRole"
                println("ROLE EXTRACTED: '$finalRole'")
                new SimpleGrantedAuthority(finalRole)
            }
        }
    }
}