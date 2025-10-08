package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permite todas as origens - IMPORTANTE: Em produção, especifique as URLs do seu frontend
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // Permite todos os métodos HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Permite todos os headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Permite credenciais (cookies, authorization headers, etc)
        configuration.setAllowCredentials(true);
        
        // Expõe os headers de resposta
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        // Tempo máximo que o resultado de uma requisição preflight pode ser cacheado
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
