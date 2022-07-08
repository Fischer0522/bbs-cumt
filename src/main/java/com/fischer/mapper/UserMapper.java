package com.fischer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.UserDO;
import org.apache.ibatis.annotations.Mapper;
/**
 * 进行基础的User相关的CRUD
 * @author fischer
 */
@Mapper
public  interface UserMapper extends BaseMapper<UserDO> {

}
