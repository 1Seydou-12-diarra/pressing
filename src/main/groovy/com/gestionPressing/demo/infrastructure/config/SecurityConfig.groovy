package com.gestionPressing.demo.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        def jwtConverter = new KeycloakJwtAuthenticationConverter()

        http
                .authorizeHttpRequests { auth ->
                    auth
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/manager/**").hasRole("MANAGER")
                            .requestMatchers("/caisse/**").hasRole("CAISSIER")
                            .requestMatchers("/atelier/**").hasRole("OPERATEUR")
                            .anyRequest().authenticated()
                }
                .oauth2ResourceServer { oauth2 ->
                    oauth2.jwt { jwt ->
                        jwt.jwtAuthenticationConverter(jwtConverter)
                    }
                }

        http.csrf().disable() // pour les API REST

        return http.build()
    }
}
