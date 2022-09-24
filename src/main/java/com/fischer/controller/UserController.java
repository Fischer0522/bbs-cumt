package com.fischer.controller;

import cn.dev33.satoken.annotation.SaCheckDisable;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.data.UpdateUserCommand;
import com.fischer.data.UpdateUserParam;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.data.LoginParam;
import com.fischer.exception.LoginException;
import com.fischer.pojo.RoleDO;
import com.fischer.pojo.UserDO;
import com.fischer.pojo.UserVO;
import com.fischer.result.ResponseResult;
import com.fischer.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fisher
 */
@RestController
@Validated
@RequestMapping("api/users")
@ResponseResult
@Slf4j
@SaCheckRole("common-user")
public class UserController {
    private UserService userService;
    private RedisService redisService;
    private EmailService emailService;
    private RoleService roleService;

    @Autowired
    UserController (UserService userService,
                    RedisService redisService,
                    EmailService emailService,
                    RoleService roleService){
        this.userService = userService;
        this.redisService = redisService;
        this.emailService = emailService;
        this.roleService = roleService;
    }

    @Transactional(rollbackFor = {SQLException.class, LoginException.class},noRollbackFor = BizException.class)
    @PostMapping( "login")
    @SaIgnore
    ResponseEntity<UserVO> loginUser( @Valid @RequestBody LoginParam loginParam){
        /*需要回滚的异常需要在核对*/

        String email = loginParam.getEmail();
        String verifyCode = redisService.getKey(email)
                .orElseThrow(() -> new BizException(401, "邮箱错误或验证码超时"));

        if(loginParam.getVerifyCode().equals(verifyCode)) {
            // 先判断当前用户是否为新用户，如果是新用户则创建用户
            boolean present = userService.getUserByEmail(loginParam.getEmail()).isPresent();
            try {
                if(!present) {
                    userService.createUser(loginParam.getEmail());
                }
                Optional<UserDO> userByEmail = userService.getUserByEmail(loginParam.getEmail());
                /*查询不到则证明刚才创建用户的过程失败*/
                UserDO userDO = userByEmail.orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));

                Long id = userDO.getId();
//                String token = jwtService.getToken(userDO);
//                redisService.saveKey(id);
                String username = userDO.getUsername();
                System.out.println("用户"+username+"正在登录");
                // 使用Sa Token进行登录
                StpUtil.login(id, SaLoginConfig.setExtra("name",userDO.getUsername()));

                // 封装权限
                LambdaQueryWrapper<RoleDO> lqw = new LambdaQueryWrapper<>();
                lqw.eq(RoleDO::getUserId,id);
                List<String> roles = roleService.list(lqw).stream()
                        .map(s -> s.getRole()).collect(Collectors.toList());

                String tokenInfo = StpUtil.getTokenInfo().getTokenValue();
                UserVO userVO = new UserVO(userDO,roles,tokenInfo);
                return ResponseEntity.ok(userVO);
            } catch (Exception e) {
                // 事务回滚只能回滚掉数据库中信创建的内容，但是redis签发的token无法处理，需重新定义异常进行处理
                String prefixKey = "loginUser:";
                String key = prefixKey+email;
                log.error("");
                log.error("登录发生异常，当前用户的Email为："+email);
                throw new LoginException(key,"登录异常",500);
            }

        } else {
            log.warn("用户输入的验证码未通过校验");
            throw new BizException(401,"邮箱或验证码错误");
        }


    }
    @SaCheckLogin
    @DeleteMapping("logout")
    ResponseEntity<Object> logoutUser() {
        StpUtil.logout();
        return ResponseEntity.ok(1);

    }
    @SaIgnore
    @GetMapping("{id}")
    ResponseEntity<UserVO>getCurrentUser(@PathVariable("id") Long id) {
        UserDO userDO = userService.getUserById(id)
                .orElseThrow(() -> new BizException(ExceptionStatus.ERROR_GET_USER_FAIL));
        LambdaQueryWrapper<RoleDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleDO::getUserId,userDO.getId());
        List<String> roles = roleService.list(lqw).stream()
                .map(s -> s.getRole()).collect(Collectors.toList());
        UserVO userVO = new UserVO(userDO,roles);
        return ResponseEntity.ok(userVO);

    }
    //@ResponseResult
    @GetMapping("email")
    @SaIgnore
    ResponseEntity<Object> getVerifyCode(@RequestParam("email") String email) {
        emailService.send(email);
        return ResponseEntity.ok(1);
    }
    @SaCheckLogin
    @PutMapping
    @SaCheckDisable("read-user")
    ResponseEntity<UserVO> updateUser(@Valid @RequestBody UpdateUserParam updateUserParam) {

        Long userId = StpUtil.getLoginIdAsLong();
        UserDO user = userService.getUserById(userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.ERROR_GET_USER_FAIL));
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(user,updateUserParam);
        UserDO userDO = userService.updateUser(updateUserCommand)
                .orElseThrow(() -> new BizException(ExceptionStatus.ERROR_UPDATE_USER_FAIL));
        LambdaQueryWrapper<RoleDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleDO::getUserId,userDO.getId());
        List<String> roles = roleService.list(lqw).stream().map(s -> s.getRole()).collect(Collectors.toList());
        UserVO userVO = new UserVO(userDO,roles);
        return ResponseEntity.ok(userVO);

    }
}
