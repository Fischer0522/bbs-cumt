package com.fischer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fischer.exception.BizException;
import com.fischer.mapper.AdjMapper;
import com.fischer.mapper.InfoMapper;
import com.fischer.mapper.UserMapper;
import com.fischer.data.UpdateUserCommand;
import com.fischer.pojo.AdjDO;
import com.fischer.pojo.InfoDO;
import com.fischer.pojo.UserDO;
import com.fischer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


/**
 * @author fischer
 */
@Slf4j
@Service
@Validated
public class UserServiceImpl implements UserService {

    private  String CACHE_KEY = "com.fischer.userInfo";
    private UserMapper userMapper;
    private InfoMapper infoMapper;
    private AdjMapper adjMapper;
    @Autowired
     public UserServiceImpl(UserMapper userMapper,
                            InfoMapper infoMapper,
                            AdjMapper adjMapper) {
        this.userMapper = userMapper;
        this.infoMapper = infoMapper;
        this.adjMapper = adjMapper;

    }

    @Override
    @Transactional(rollbackFor = {SQLException.class,RuntimeException.class})
    public synchronized UserDO createUser(String email) {
        try {
            InfoDO info = infoMapper.getInfo();
            AdjDO adj = adjMapper.getAdj();
            String randNickname = adj.getWord()+info.getRandName();
            UserDO userDO = new UserDO(randNickname,email,info.getRandImage());
            // LambdaUpdateWrapper<InfoDO> lqw = new LambdaUpdateWrapper<>();
            info.setUsed(info.getUsed()+1);
            int insert = userMapper.insert(userDO);
            infoMapper.updateById(info);
            adj.setUsed(adj.getUsed()+1);
            // 用户名和前缀的使用次数均+1，继续生成下一个用户名
            adjMapper.updateById(adj);
            // 保证获取id和对形容词修改的一致性，防止一个形容词被多人使用
            log.info("创建用户成功，生成的用户名为:"+randNickname);
            return userDO;

        } catch (RuntimeException e){
            // 抛出异常,回滚掉
            throw new BizException(500,"用户创建失败");
        }

    }

    @Cacheable(cacheNames = "user",key = "'com.fischer.userInfo:'+ #id")
    @Override
    public Optional<UserDO> getUserById(Integer id) {
        UserDO userDO = userMapper.selectById(id);
        return Optional.ofNullable(userDO);
    }

    @Override
    public Optional<UserDO> getUserByEmail(String email) {

        LambdaQueryWrapper<UserDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserDO::getEmail,email);
        UserDO userDO = userMapper.selectOne(lqw);
        return Optional.ofNullable(userDO);
    }

    @Override
    public List<UserDO> searchUser(String username) {
        LambdaQueryWrapper<UserDO> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(username),UserDO::getUsername,username);
        List<UserDO> userList = userMapper.selectList(lqw);
        return userList;
    }

    @Override
    public Integer getUserCount() {
        Integer integer = userMapper.selectCount(null);
        return integer;
    }

    @CachePut(cacheNames = "user",key = "'com.fischer.userInfo:'+ #updateUserCommand.targetUser.id")
    @Override
    public Optional<UserDO> updateUser(@Valid UpdateUserCommand updateUserCommand) {
        UserDO userDO = updateUserCommand.getTargetUser();
        String username = updateUserCommand.getUpdateUserParam().getUsername();
        String image = updateUserCommand.getUpdateUserParam().getImage();

        if(Strings.isNotEmpty(username)) {
            userDO.setUsername(username);
        }
        if(Strings.isNotEmpty(image)) {
            userDO.setImage(image);
        }

        int i = userMapper.updateById(userDO);
        if(i > 0) {
            return Optional.of(userDO);
        } else {
            return Optional.empty();
        }
    }

}
