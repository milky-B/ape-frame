package com.airport.ape.user.mapper;

import com.airport.ape.user.entity.dto.SysUserDto;
import com.airport.ape.user.entity.po.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-08 20:06:02
 */
@Mapper
public interface SysUserBakMapper extends BaseMapper<SysUser> {
    @Insert("<script>" +
            " insert into sys_user_bak(id,name,age) " +
            " values " +
            "<foreach collection=\"entities\" item=\"entity\" separator=\",\">" +
            " (#{entity.id},#{entity.name}, #{entity.age}) " +
            "</foreach>" +
            "</script>")
    int insertBatch(@Param("entities")List<SysUser> sysUserList);
}

