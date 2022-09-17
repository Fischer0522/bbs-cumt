package com.fischer.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.mapper.RoleMapper;
import com.fischer.pojo.RoleDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<String> getPermissionList(Object loginId, String s) {

        return null;

    }

    @Override
    public List<String> getRoleList(Object loginId, String s) {
        LambdaQueryWrapper<RoleDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleDO::getUserId, loginId);
        List<RoleDO> roleDOS = roleMapper.selectList(lqw);

        return roleDOS.stream()
                .map(RoleDO::getRole).collect(Collectors.toList());

    }
}
