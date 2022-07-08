package com.fischer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.FavoriteDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fischer*/
@Mapper
public interface FavoriteMapper extends BaseMapper<FavoriteDO> {
}
