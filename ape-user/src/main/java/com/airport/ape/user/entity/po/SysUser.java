package com.airport.ape.user.entity.po;

import com.airport.ape.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * (SysUser)实体类
 *
 * @author makejava
 * @since 2023-11-08 20:06:03
 */
@TableName("sys_user")
@Data
public class SysUser extends BaseEntity {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;
    
    private String name;
    
    private Integer age;

}

