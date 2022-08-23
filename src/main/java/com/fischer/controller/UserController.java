package com.fischer.controller;

import com.fischer.data.UpdateUserCommand;
import com.fischer.data.UpdateUserParam;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.data.LoginParam;
import com.fischer.exception.LoginException;
import com.fischer.pojo.UserDO;
import com.fischer.pojo.UserVO;
import com.fischer.result.ResponseResult;
import com.fischer.service.EmailService;
import com.fischer.service.JwtService;
import com.fischer.service.RedisService;
import com.fischer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author fisher
 */
@RestController
@RequestMapping("users")
@ResponseResult
@Validated
@Slf4j
public class UserController {
    private UserService userService;
    private RedisService redisService;
    private JwtService jwtService;
    private EmailService emailService;

    @Autowired
    UserController (UserService userService,
                    RedisService redisService,
                    JwtService jwtService,
                    EmailService emailService){
        this.userService = userService;
        this.redisService = redisService;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    @Transactional(rollbackFor = {SQLException.class, LoginException.class},noRollbackFor = BizException.class)
    @PostMapping(path = "login")
    ResponseEntity<UserVO> loginUser(@Valid @RequestBody LoginParam loginParam){
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

                Integer id = userDO.getId();
                String token = jwtService.getToken(userDO);
                redisService.saveKey(id);
                UserVO userVO = new UserVO(userDO,token);
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

    @DeleteMapping("logout")
    ResponseEntity<UserVO> logoutUser(@RequestHeader(value = "Authorization") String token) {
        UserDO user = jwtService.getUser(token);
        UserDO userDO = userService.getUserById(user.getId())
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        redisService.deleteKey(user.getId());
        UserVO userVO = new UserVO(userDO,token);
        return ResponseEntity.ok(userVO);

    }

    @GetMapping("{id}")
    ResponseEntity<UserDO>getCurrentUser(@PathVariable("id") Integer id) {
        UserDO userDO = userService.getUserById(id)
                .orElseThrow(() -> new BizException(ExceptionStatus.NOT_FOUND));
        return ResponseEntity.ok(userDO);

    }

    @GetMapping("email")
    ResponseEntity<Object> getVerifyCode(@RequestParam("email") String email) {
        emailService.send(email);
        return ResponseEntity.ok(null);
    }

    @ResponseResult
    @PutMapping
    ResponseEntity<UserDO> updateUser(@Valid @RequestBody UpdateUserParam updateUserParam,
                                      @RequestHeader("Authorization") String token) {

        UserDO user = jwtService.getUser(token);
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(user,updateUserParam);
        UserDO userDO = userService.updateUser(updateUserCommand)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(userDO);

    }
}
