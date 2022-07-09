package com.fischer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fischer.mapper.AdjMapper;
import com.fischer.mapper.InfoMapper;
import com.fischer.mapper.UserMapper;
import com.fischer.pojo.AdjDO;
import com.fischer.pojo.InfoDO;
import com.fischer.pojo.UserDO;
import com.fischer.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * @author fischer
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
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
    public UserDO createUser(String email) {
        try {
            InfoDO info = infoMapper.getInfo();
            AdjDO adj = adjMapper.getAdj();
            String randNickname = adj.getWord()+info.getRandName();
            UserDO userDO = new UserDO(randNickname,email,info.getRandImage());
            LambdaUpdateWrapper<InfoDO> lqw = new LambdaUpdateWrapper<>();
            info.setUsed(info.getUsed()+1);
            int insert = userMapper.insert(userDO);
            infoMapper.updateById(info);
            adj.setUsed(adj.getUsed()+1);
            adjMapper.updateById(adj);
            return userDO;

        } catch (RuntimeException e){
            /*后续改为自定义的异常*/
            throw e;
        }

    }

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
}
