package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDO {
    private Long userId;
    private String role;
}
