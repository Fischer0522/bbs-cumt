package com.fischer.service;

import org.springframework.stereotype.Service;

/**
 * @author fisher
 */
@Service
public interface RedisService {
/** 将user对应的Id存入 redis 设置七天过期时间
 * @param id 当前准备登录的用户的id
 */
    void saveKey(Integer id);
   /**将对应的key从redis中移除
    * @param id 需要删除的key
    */
    void deleteKey(Integer id);

    /**从redis中查询是否存在该key
     *@param id  查询的id
     * @return 对应的value
     */
    String getKey(Integer id);
}
