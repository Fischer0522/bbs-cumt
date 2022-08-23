package com.fischer.controller;

import com.fischer.data.UpdateUserCommand;
import com.fischer.data.UpdateUserParam;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.data.LoginParam;
import com.fischer.pojo.UserDO;
import com.fischer.pojo.UserVO;
import com.fischer.result.ResponseResult;
import com.fischer.service.EmailService;
import com.fischer.service.JwtService;
import com.fischer.service.RedisService;
import com.fischer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author fisher
 */
@RestController
@RequestMapping("users")
@ResponseResult
@Validated
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

    @Transactional(rollbackFor = {Exception.class})
    @PostMapping(path = "login")
    ResponseEntity<UserVO> loginUser(@Valid @RequestBody LoginParam loginParam){
        /*需要回滚的异常需要在核对*/
        boolean present = userService.getUserByEmail(loginParam.getEmail()).isPresent();
        if(!present) {
            userService.createUser(loginParam.getEmail());
        }
        String email = loginParam.getEmail();
        String verifyCode = redisService.getKey(email)
                .orElseThrow(() -> new BizException(401, "邮箱错误或验证码超时"));
        Optional<UserDO> userByEmail = userService.getUserByEmail(loginParam.getEmail());
        /*查询不到则证明刚才创建用户的过程失败*/
        UserDO userDO = userByEmail.orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        if(loginParam.getVerifyCode().equals(verifyCode)) {
            /*验证码一致，签发token，完成登录*/
            Integer id = userDO.getId();
            String token = jwtService.getToken(userDO);
            redisService.saveKey(id);
            UserVO userVO = new UserVO(userDO,token);
            return ResponseEntity.ok(userVO);
        } else {
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
