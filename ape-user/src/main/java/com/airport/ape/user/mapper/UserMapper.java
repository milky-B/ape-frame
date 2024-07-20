package com.airport.ape.user.mapper;

import com.airport.ape.user.entity.dto.UserWithWorkOrderDto;
import com.airport.ape.user.entity.po.UserPo;
import com.airport.ape.user.entity.po.WarehouseWorkOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserPo> {
    IPage<UserPo> selectUserPage(IPage<UserPo> userPoIPage);


    /*@Select("<script>" +
            "select u.id,u.name,wwo.work_type,wwo.goods_operate_time,wwo.finish_time " +
            "from user u " +
            "left join warehouse_work_order wwo on wwo.outsourced_user_id=u.id " +
            "<if test=\"start!=null and start!=''\"> and wwo.finish_time >= #{start} </if>"  +
            "<if test=\"end!=null and end!=''\"> and wwo.finish_time &lt;= #{end} </if>"  +
            "where role=#{supplierId}" +
            "</script>")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "id",property = "warehouseWorkOrder",
                    many = @Many(select = "com.airport.ape.user.mapper.WarehouseWorkOrderMapper.selectByUerId"))
    })*/
    /*@Results({
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "work_type",property = "warehouseWorkOrder.workType"),
            @Result(column = "goods_operate_time",property = "warehouseWorkOrder.goodsOperateTime"),
            @Result(column = "finish_time",property = "warehouseWorkOrder.finishTime")
    })*/

    List<UserWithWorkOrderDto> queryUserWithWorkOrderBySupplierId(@Param("supplierId")Long supplierId, @Param("start") String start, @Param("end") String end);

    @Select("select id,name " +
            "from user " +
            "where role=#{id}")
    List<UserWithWorkOrderDto> selectUserList(Long id);



    @Results(id = "userWithWorkOrderDtoResultMap", value = {
            @Result(property = "id", column = "user_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "warehouseWorkOrder", column = "user_id",
                    javaType = List.class,
                    many = @Many(select = "com.airport.ape.user.mapper.UserMapper.queryWarehouseWorkOrders",fetchType = FetchType.LAZY))
    })
    @Select("<script>" +
            "SELECT u.id user_id, u.name " +
            "FROM user u " +
            /*"LEFT JOIN warehouse_work_order wwo ON wwo.outsourced_user_id = u.id " +
            "<if test='start != null and start != \"\"'>" +
            "AND wwo.finish_time >= #{start}" +
            "</if>" +
            "<if test='end != null and end != \"\"'>" +
            "AND wwo.finish_time &lt;= #{end}" +
            "</if>" +*/
            "where u.role = #{supplierId}" +
            "</script>")
    List<UserWithWorkOrderDto> queryUserWithWorkOrderBySupplierIdBak(@Param("supplierId") Long supplierId,
                                                                 @Param("start") String start,
                                                                  @Param("end") String end);

    @Results(id = "warehouseWorkOrderResultMap", value = {
            @Result(property = "id", column = "w_id"),
            @Result(property = "workType", column = "work_type"),
            @Result(property = "goodsOperateTime", column = "goods_operate_time"),
            @Result(property = "finishTime", column = "finish_time")
    })
    @Select("<script>" +
            "SELECT id AS w_id, work_type, goods_operate_time, finish_time " +
            "FROM warehouse_work_order " +
            "WHERE outsourced_user_id = #{userId} " +
            "<if test='start != null and start != \"\"'>" +
            "AND finish_time >= #{start}" +
            "</if>" +
            "</script>")
    List<WarehouseWorkOrder> queryWarehouseWorkOrders(@Param("userId") Long userId,
                                                      @Param("start") String start);

    Integer selectCountByEntity(UserPo userPo);
}