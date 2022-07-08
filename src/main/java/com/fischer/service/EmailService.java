package com.fischer.service;

import org.springframework.stereotype.Service;

/**
 * @author fischer
 */
@Service
public interface EmailService {
    /** 调用STMP协议向对应的邮箱发送验证码
     * @param email 需获取验证码的邮箱*/
    void send(String email);

}
