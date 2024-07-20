package com.airport.ape.user.mapper;

import com.airport.ape.user.entity.po.Classes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClassMapper {
    @Select("select id,name " +
            "from class ")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "id",property = "students",
                    many = @Many(select = "com.airport.ape.user.mapper.StudentMapper.selectStudents"))
    })
    List<Classes> selectClasses();
}
