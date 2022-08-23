package com.fischer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author fischer
 */
@EnableTransactionManagement
@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class RgznIcuApplication {

    @Bean
    public JavaMailSenderImpl javaMailSender(){
        JavaMailSenderImpl jms=new JavaMailSenderImpl();
        return jms;
    }

    public static void main(String[] args) {
        SpringApplication.run(RgznIcuApplication.class, args);
    }

}
