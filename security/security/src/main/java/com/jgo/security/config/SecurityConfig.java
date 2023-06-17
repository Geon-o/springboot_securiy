package com.jgo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
public class SecurityConfig{
    // 스프링 시큐리티 필터
    /**
     * WebSecurityconfigurerAdapter deprecated 되어
     * 다음과 같이 SecurityFilterChain을 빈으로 등록(권장)
     * */

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logout -> logout.logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll());
        httpSecurity.authorizeHttpRequests(authz -> authz
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/manager/**").hasAnyRole("ADMIN or MANAGER")
                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                .anyRequest().permitAll()
        ).formLogin(formLogin -> formLogin.loginPage("/loginForm"));

        return httpSecurity.build();
    }
}
