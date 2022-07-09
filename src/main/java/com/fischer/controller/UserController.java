package com.fischer.controller;

import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.pojo.LoginParam;
import com.fischer.pojo.UserDO;
import com.fischer.pojo.UserVO;
import com.fischer.service.EmailService;
import com.fischer.service.JwtService;
import com.fischer.service.RedisService;
import com.fischer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author fisher
 */
@RestController
@RequestMapping("users")
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
    /*需要回滚的异常需要在核对*/
    @Transactional(rollbackFor = {Exception.class})
    @PostMapping(path = "login")
    ResponseEntity<UserVO> loginUser(LoginParam loginParam){
        boolean present = userService.getUserByEmail(loginParam.getEmail()).isPresent();
        if(present) {
            userService.createUser(loginParam.getEmail());
        }
        Optional<UserDO> userByEmail = userService.getUserByEmail(loginParam.getEmail());
        UserDO userDO = userByEmail.orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        Integer id = userDO.getId();
        String token = jwtService.getToken(userDO);
        redisService.saveKey(id);
        UserVO userVO = new UserVO(userDO,token);
        return ResponseEntity.ok(userVO);

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
}
