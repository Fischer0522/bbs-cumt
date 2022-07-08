package com.fischer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestEmailService {
    private EmailService emailService;
    @Autowired
    public TestEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Test
    void testSend() {
        String email ="1809327837@qq.com";
        emailService.send(email);
    }
}
