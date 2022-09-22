package com.fischer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.CommentFavoriteDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentFavoriteMapper extends BaseMapper<CommentFavoriteDO> {
}
