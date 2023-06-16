package com.jgo.security.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * templates에 있는 html 파일을 mustache로 변경하기 위한 설정
     *
     * @param registry
     * @since 4.1
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        MustacheViewResolver resolver = new MustacheViewResolver();
        resolver.setCharset("UTF-8");
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");

        registry.viewResolver(resolver);
    }
}
