package com.example.demo.security;

import com.example.demo.user.dao.UsuarioDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UsuarioDAO usuarioDAO;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    // Injetando o handler no construtor
    public SecurityConfig(UsuarioDAO usuarioDAO, CustomAccessDeniedHandler accessDeniedHandler) {
        this.usuarioDAO = usuarioDAO;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**").disable())
            .headers(headers -> headers.frameOptions().disable()) // Habilita o uso do H2 Console
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/usuarios/login", "/h2-console/**", "/usuarios/criar-senha", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> 
                exception.accessDeniedHandler(accessDeniedHandler)  // Aqui!
            )
            .addFilterBefore(new JwtAuthenticationFilter(usuarioDAO), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
