package com.fischer.service;

import com.fischer.pojo.UserDO;

import java.util.List;
import java.util.Optional;
/**@author fischer
 */
public interface UserService {
    /** 按邮箱创建用户
     * @param email 提交邮箱，无密码登录
     * @return UserDo,基础类型
     */
    UserDO createUser(String email);
    /** 按id查询用户
     * @param id 用户Id
     * @return Optional进行空判断
     */
    Optional<UserDO> gerUserById(Integer id);
    /** 按用户名模糊查询
     * @param username 用户名
     * @return 以list形式返回所有的匹配结果
     */
    List<UserDO> searchUser(String username);
    /** 统计注册的所有人数
     * @return 人数
     */
    Integer getUserCount();

}
