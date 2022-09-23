package com.fischer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.CommentReplyDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentReplyMapper extends BaseMapper<CommentReplyDO> {
}
