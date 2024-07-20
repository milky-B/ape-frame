package com.airport.ape.user.mapper;

import com.airport.ape.user.entity.po.Student;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {
    @Select("select id,name " +
            "from student " +
            "where class_id=#{classId}")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name")
    })
    List<Student> selectStudents(Integer classId);
}
