package com.fischer.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.pojo.*;
import com.fischer.result.ResponseResult;
import com.fischer.service.ArticleService;
import com.fischer.service.RoleService;
import com.fischer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin")
@ResponseResult
public class AdminController {
    private final String[] disableAction ={"article","comment","login","read-article","read-comment","read-user","file"};
    private UserService userService;
    private RoleService roleService;
    private ArticleService articleService;
    @Autowired
    public AdminController (UserService userService,
                            RoleService roleService,
                            ArticleService articleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.articleService = articleService;
    }
    /**
     * @param level:
     *             level0为禁止写文章
     *             level1为禁止评论
     *             level2禁止登录
     *             level3禁止读文章
     *             level4禁止读评论
     *             level5禁止查看用户详情
     *             level6禁止上传文件
     *
     * */
    @SaCheckRole("admin")
    @GetMapping("disable/{userId}")
    public ResponseEntity<UserVO> disableUser(@PathVariable("userId") Long userId,
                                              @RequestParam("level") Integer level,
                                              @RequestParam("second") Long second) {
        StpUtil.disable(userId,disableAction[level],second);
        UserDO userDO = userService.getUserById(userId).
                orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        LambdaQueryWrapper<RoleDO> lqw = new LambdaQueryWrapper<>();

        lqw.eq(RoleDO::getUserId,userId);
        List<String> roles = roleService.list(lqw).stream()
                .map(s -> s.getRole()).collect(Collectors.toList());
        UserVO userVO = new UserVO(userDO,roles);
        return ResponseEntity.ok(userVO);

    }
    /**
     * @param status 文章的状态0为普通状体，1为置顶 2为屏蔽*/
    @PutMapping("status/{articleId}")
    @SaCheckRole("admin")
    public ResponseEntity<ArticleBO> changeArticleStatus(@PathVariable("articleId") Long articleId,@RequestParam("status") Integer status) {
        ArticleDO articleDO = articleService.getArticleDOById(articleId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        articleDO.setStatus(status);
        articleService.updateById(articleDO);
        UserDO userDO = userService.getUserById(articleDO.getUserId())
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(new ArticleBO(articleDO,userDO,false,0));


    }
    @DeleteMapping("kickout/{userId}")
    @SaCheckRole("admin")
    public ResponseEntity<UserVO> kickout(@PathVariable("userId") Long userId) {
        StpUtil.kickout(userId);
        LambdaQueryWrapper<RoleDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleDO::getUserId,userId);
        List<String> roles = roleService.list(lqw).stream().map(s -> s.getRole()).collect(Collectors.toList());
        UserVO userVO = userService.getUserById(userId)
                .map(s -> new UserVO(s, roles))
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(userVO);
    }

    @PostMapping("add/{userId}")
    @SaCheckRole("super-admin")
    public ResponseEntity<UserVO> addAdmin(@PathVariable Long userId){
        RoleDO roleDO = new RoleDO(userId,"admin");
        roleService.saveOrUpdate(roleDO);
        LambdaQueryWrapper<RoleDO> lqw = new LambdaQueryWrapper();
        lqw.eq(RoleDO::getUserId,userId);

        List<String> roles = roleService.list(lqw).stream().map(s -> s.getRole()).collect(Collectors.toList());
        UserDO userDO = userService.getUserById(userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        UserVO userVO = new UserVO(userDO,roles);
        return ResponseEntity.ok(userVO);

    }

}
