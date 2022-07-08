package com.fischer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.InfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用于获取随机的匿名，和修改使用次数，与AdjMapper配合
 * @author fischer
 */
@Mapper
public interface InfoMapper extends BaseMapper<InfoDO> {
/** 生成随机名字
 * @return 返回形式为InfoDO，包含匿名和使用次数
 */
    InfoDO getInfo();

}
