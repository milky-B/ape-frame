package com.airport.ape.user.entity.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserReq {
    @ApiModelProperty(value = "用户名字",required = true)
    private String name;
    @ApiModelProperty(value = "年龄",required = true)
    private int age;
}
