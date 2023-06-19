package com.jgo.security.config;

import com.jgo.security.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig{
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;
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
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.logout(logout -> logout.logoutUrl("/logout")
                .logoutSuccessUrl("/loginForm?logout")
                .permitAll());
        httpSecurity.authorizeHttpRequests(authz -> authz
                .requestMatchers("/user/**").authenticated()//인증만 되면 들어갈 수 있는 주소
                .requestMatchers("/manager/**").hasAnyRole("MANAGER","ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
        ).formLogin(formLogin ->
                formLogin.loginPage("/loginForm")
                .loginProcessingUrl("/login")// '/login' 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해줌
                .defaultSuccessUrl("/"))
                .oauth2Login(oauth2 -> oauth2.loginPage("/loginForm")
                        .userInfoEndpoint(userInfoEndPoint -> userInfoEndPoint.userService(principalOauth2UserService))); // 구글 로그인이 완료된 뒤의 후처리가 필요

        return httpSecurity.build();
    }
}
