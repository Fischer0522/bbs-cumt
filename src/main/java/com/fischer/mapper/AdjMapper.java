package com.fischer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.AdjDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fisher
 */
@Mapper
public interface AdjMapper extends BaseMapper<AdjDO> {
    /**
     * 用于生成随机的形容词
     * @return  返回随机形容其
     * */
    AdjDO getAdj();

}
