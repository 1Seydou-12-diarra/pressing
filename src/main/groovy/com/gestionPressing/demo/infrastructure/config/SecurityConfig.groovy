package com.gestionPressing.demo.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
class SecurityConfig {

    /**
     * JwtDecoder configuré pour RS256 via Keycloak JWKS
     */
    @Bean
    NimbusJwtDecoder jwtDecoder() {
        def jwkSetUri = "http://localhost:8180/realms/pressing-realm/protocol/openid-connect/certs"
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build()
    }

    /**
     * Définition de la sécurité HTTP
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/manager/**").hasAuthority("ROLE_MANAGER")
                        .requestMatchers("/caisse/**").hasAuthority("ROLE_CAISSIER")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt { jwt ->
                            jwt.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())
                        }
                )
        return http.build()
    }
}