package com.fischer.service;

import com.fischer.pojo.UserDO;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author fisher
 * 用于解析和生成token
 */

@Service
public interface JwtService {
    /** 通过传入的token解析出对应的User
     * @param token 请求携带的token
     * @return 获取原始的数据库中的字段信息*/
    UserDO getUser(String token);
    /** 通过User信息生成对应的token
     * @param userDO user基础信息
     * @return 返回字符串类型的token
     */
    String getToken(UserDO userDO);

    /** 从token中获取相关用户Id
     * @param token 请求携带的token
     * @return 返回用户的id*/
    Optional<Integer> getSubFromToken(String token);
}
