package com.fischer.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author fisher
 */
@Service
public interface RedisService {
/** 将user对应的Id存入 redis 设置七天过期时间
 * @param id 当前准备登录的用户的id
 */
    void saveKey(Long id);
   /**将对应的key从redis中移除
    * @param id 需要删除的key
    */
    void deleteKey(Long id);

    /**从redis中查询是否存在该key,针对用检验用户是否已经登录
     *@param id  查询的id
     * @return 对应的value
     */
    Optional<String> getKey(Long id);

    /** 从redis中获取对应的验证码，用于进行登录
     * @param email 用户邮箱
     * @return 对应的验证码*/
    Optional<String> getKey(String email);
}
