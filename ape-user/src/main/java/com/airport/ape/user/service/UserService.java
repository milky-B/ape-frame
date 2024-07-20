package com.airport.ape.user.service;

import com.airport.ape.mybatis.entity.PageResult;
import com.airport.ape.user.entity.dto.UserDto;
import com.airport.ape.user.entity.dto.UserWithWorkOrderDto;
import com.airport.ape.user.entity.po.UserPo;
import io.swagger.models.auth.In;

import java.util.List;


public interface UserService {
    int addUser(UserDto userDto);

    int delUser(Integer id);

    PageResult<UserPo> queryUserList(UserDto userDto);

    void test();

    List<UserWithWorkOrderDto> userWithWorkOrder(Long id, String start, String end);
    List<UserWithWorkOrderDto> test(Long id, String start, String end);

    Integer count(UserPo userPo);
}
