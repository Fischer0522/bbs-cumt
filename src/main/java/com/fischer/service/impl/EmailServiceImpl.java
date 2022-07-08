package com.fischer.service.impl;

import com.fischer.service.EmailService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

/**
 * @author fisher
 */
@Service
@NoArgsConstructor
public class EmailServiceImpl implements EmailService {
    private StringRedisTemplate stringRedisTemplate;
    private JavaMailSenderImpl javaMailSender;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.password}")
    private String password;

    @Autowired
    public EmailServiceImpl(StringRedisTemplate stringRedisTemplate,
                            JavaMailSenderImpl javaMailSender) {
        this.javaMailSender = javaMailSender;
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void send(String email) {

        double random = Math.random();
        int code = (int) (999+random*9000);
        Duration duration = Duration.ofMinutes(5);
        stringRedisTemplate.opsForValue().set(email,code+"");
        stringRedisTemplate.expire(email,duration);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setHost(host);
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("登录验证码");
        simpleMailMessage.setText("请在5分钟内尽快填写验证码，完成注册\n"+"验证码为："+code);
        javaMailSender.send(simpleMailMessage);

    }

}
