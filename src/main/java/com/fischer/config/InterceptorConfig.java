package com.fischer.config;

import com.fischer.intercepter.RequestInterceptor;
import com.fischer.intercepter.RestrainInterceptor;
import com.fischer.intercepter.ResultInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author fisher
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        //添加映射路径
        registry.addMapping("/**")
                //是否发送Cookie
                .allowCredentials(true)
                //设置放行哪些原始域   SpringBoot2.4.4下低版本使用.allowedOrigins("*")
                .allowedOrigins("*")
                //放行哪些请求方式
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")
                //.allowedMethods("*") //或者放行全部
                //放行哪些原始请求头部信息
                .allowedHeaders("*")
                //暴露哪些原始请求头部信息
                .exposedHeaders("*")
                .maxAge(3600);
    }*/



    @Bean
    public ResultInterceptor resultInterceptor() {
        return new ResultInterceptor();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor();
    }

    @Bean
    public RestrainInterceptor restrainInterceptor() {
        return new RestrainInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {




        registry.addInterceptor(requestInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/users/email")
                .excludePathPatterns("/users/login")
                .excludePathPatterns("/users/{id}")
                .excludePathPatterns("/articles/exact")
                .excludePathPatterns("/articles/fuzzy")
                .excludePathPatterns("/comments/{articleId}")
                .excludePathPatterns("/articles/{articleId}")
                .excludePathPatterns("/error");


        registry.addInterceptor(restrainInterceptor())
                .addPathPatterns("/articles/**")
                .addPathPatterns("/comments/**")
                .excludePathPatterns("/articles/exact")
                .excludePathPatterns("/articles/fuzzy")
                .excludePathPatterns("/comments/{articleId}")
                .excludePathPatterns("/articles/{articleId}");


        registry.addInterceptor(resultInterceptor())
                .addPathPatterns("/**");


    }
}
