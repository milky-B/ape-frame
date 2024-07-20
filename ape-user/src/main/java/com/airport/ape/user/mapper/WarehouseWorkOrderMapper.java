package com.airport.ape.user.mapper;

import com.airport.ape.user.entity.po.WarehouseWorkOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WarehouseWorkOrderMapper {
    @Select("select * from warehouse_work_order where outsourced_user_id=#{userId} and finish_time >=#{start} and finish_time <=#{end}")
    @Results({
            @Result(column = "work_type",property = "workType"),
            @Result(column = "goods_operate_time",property = "goodsOperateTime"),
            @Result(column = "finish_time",property = "finishTime")
    })
    List<WarehouseWorkOrder> selectByUerId(@Param("userId")Long userId,@Param("start") String start,@Param("end") String end);
}
