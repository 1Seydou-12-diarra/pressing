import com.gestionPressing.demo.infrastructure.config.KeycloakJwtAuthenticationConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
class SecurityConfig {

    @Bean
    NimbusJwtDecoder jwtDecoder() {
        def jwkSetUri = "http://localhost:8180/realms/pressing-realm/protocol/openid-connect/certs"
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build()
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        def config = new CorsConfiguration()
        config.allowedOrigins = ["http://localhost:5173"]
        config.allowedMethods = ["GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"]
        config.allowedHeaders = ["*"]
        config.allowCredentials = true
        def source = new UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/manager/**").hasAuthority("ROLE_MANAGER")
                        .requestMatchers("/caisse/**").hasAuthority("ROLE_CAISSIER")
                        .requestMatchers("/employe/**").hasAuthority("ROLE_EMPLOYE")

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