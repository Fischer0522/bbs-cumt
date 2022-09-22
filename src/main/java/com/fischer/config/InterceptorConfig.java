package com.fischer.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.fischer.intercepter.RestrainInterceptor;
import com.fischer.intercepter.ResultInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fisher
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    // 对结果进行统一处理
    @Bean
    public ResultInterceptor resultInterceptor() {
        return new ResultInterceptor();
    }
    // 限制请求次数
    @Bean
    public RestrainInterceptor restrainInterceptor() {
        return new RestrainInterceptor();
    }

    // 开启权限认证
    @Bean
    public SaInterceptor saInterceptor() {
        return  new SaInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {



        registry.addInterceptor(restrainInterceptor())
                .addPathPatterns("/api/articles/**")
                .addPathPatterns("/api/comments/**")
                .excludePathPatterns("/api/articles/exact")
                .excludePathPatterns("/api/articles/fuzzy")
                .excludePathPatterns("/api/comments/{articleId}")
                .excludePathPatterns("/api/articles/{articleId}");


        registry.addInterceptor(resultInterceptor())
                .addPathPatterns("/**");



        registry.addInterceptor(saInterceptor())
                .addPathPatterns("/**");





    }
}
