package ticket.store.backend.config.security;

import lombok.RequiredArgsConstructor;
import ticket.store.backend.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtFilterAuthenticationEntryPoint;
    private final AuthzAccessDeniedHandler authzAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/user/{userId}")
                            .access(new WebExpressionAuthorizationManager("#userId == authentication.principal.toString()"))
                        .requestMatchers(HttpMethod.POST, "/api/event")
                                .access(new WebExpressionAuthorizationManager("hasRole('ORGANIZER') and (new String(request.getPart('organizerId').getInputStream().readAllBytes()) == authentication.principal.toString())"))
//                            .access(new WebExpressionAuthorizationManager("hasRole('ORGANIZER') and (#httpServletRequest.getParameter('organizerId') == authentication.principal.toString())"))
                        .requestMatchers("/api/auth/logout/{userId}")
                            .access(new WebExpressionAuthorizationManager("#userId == authentication.principal.toString()"))
                        .requestMatchers(regexMatcher("^/api/event\\?(?=.*\\borganizerId=[^&]+\\b)(?=.*\\bisConfirmed=false\\b).*"))
                            .access(new WebExpressionAuthorizationManager("hasRole('ADMIN') or (hasRole('ORGANIZER') and request.getParameter('organizerId') == authentication.principal.toString())"))
                        .requestMatchers(regexMatcher("^/api/event\\?(?=.*\\bisConfirmed=false\\b).*$")).hasRole("ADMIN")
                        .requestMatchers("/api/event/{eventId}/confirm").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, AnonymousAuthenticationFilter.class)
                .cors(Customizer.withDefaults())
                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(jwtFilterAuthenticationEntryPoint)
                        .accessDeniedHandler(authzAccessDeniedHandler)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
