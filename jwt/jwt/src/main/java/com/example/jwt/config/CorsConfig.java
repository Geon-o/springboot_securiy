package com.example.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true); //내서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지 설정
        configuration.addAllowedOrigin("*"); // 모든 ip에 응답을 허용
        configuration.addAllowedHeader("*"); // 모든 header에 응답을 허용
        configuration.addAllowedMethod("*"); // 모든 restApi의 요청을 허용
        source.registerCorsConfiguration("/api/**", configuration);

        return new CorsFilter(source);
    }
}
