package com.fischer.service;

import com.fischer.param.UpdateUserCommand;
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
    Optional<UserDO> getUserById(Integer id);
    /** 按邮箱进行重复性匹配
     * @param email 用户邮箱
     * @return optional用于处理查询为空的结果*/
    Optional<UserDO> getUserByEmail(String email);

    /** 按用户名模糊查询
     * @param username 用户名
     * @return 以list形式返回所有的匹配结果
     */
    List<UserDO> searchUser(String username);
    /** 统计注册的所有人数
     * @return 人数
     */
    Integer getUserCount();
    /** 更新用户基本信息
     * @param updateUserCommand 要更新的目标用户和更新表单，用于进行校验
     * @return 返回更新后的用户信息*/
    Optional<UserDO> updateUser(UpdateUserCommand updateUserCommand);

}
