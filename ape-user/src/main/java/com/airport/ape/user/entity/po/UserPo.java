package com.airport.ape.user.entity.po;

import com.airport.ape.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.models.auth.In;
import lombok.Data;


@TableName("user")
@Data
public class UserPo extends BaseEntity {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private Integer age;
}
