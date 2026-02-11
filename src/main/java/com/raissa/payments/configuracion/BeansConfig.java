package com.raissa.payments.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class BeansConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.setAllowCredentials(true);

        cors.addAllowedOrigin("https://www.raissaob.com");
        cors.addAllowedOrigin("https://raissaob.com");
        cors.addAllowedOrigin("https://3.136.16.248");
        cors.addAllowedOrigin("https://3.136.16.248:8090");
        cors.addAllowedOrigin("http://3.136.16.248:8090");
        cors.addAllowedOrigin("http://3.136.16.248:90");
        cors.addAllowedOrigin("http://3.136.16.248");
        cors.addAllowedOrigin("http://127.0.0.3:4200");
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
}
