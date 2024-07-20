package com.airport.ape.user.mapper;

import com.airport.ape.user.entity.po.FileLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface FileLogMapper extends BaseMapper<FileLog> {

    @Select("select id,path " +
            "from file_log " +
            "where waybill = #{waybill} " +
            "limit 1")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "path",property = "path")
    })
    FileLog selectByWaybillId(Long waybill);
}
