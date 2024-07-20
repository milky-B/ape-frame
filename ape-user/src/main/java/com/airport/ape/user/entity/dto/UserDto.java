package com.airport.ape.user.entity.dto;

import lombok.Data;

@Data
public class UserDto {
    private String name;

    private Integer age;

    private Integer pageIndex = 1;

    private Integer size = 10;
}
