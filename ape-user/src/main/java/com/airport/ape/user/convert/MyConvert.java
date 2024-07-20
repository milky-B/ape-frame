package com.airport.ape.user.convert;

import com.airport.ape.user.entity.dto.UserDto;
import com.airport.ape.user.entity.po.UserPo;
import com.airport.ape.user.entity.req.UserReq;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MyConvert {
    MyConvert INSTANCE = Mappers.getMapper(MyConvert.class);
    UserPo toUserPo(UserDto userDto);
    UserDto toUserDto(UserReq userReq);
}
