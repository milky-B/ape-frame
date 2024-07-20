package com.airport.ape.user.service;

import com.airport.ape.web.entity.PageResponse;
import com.airport.ape.user.entity.dto.SysUserDto;
import com.airport.ape.user.entity.dto.UserDto;
import com.airport.ape.user.entity.po.SysUser;

import java.util.List;
import java.util.Map;


/**
 * (SysUser)表服务接口
 *
 * @author makejava
 * @since 2023-11-08 20:06:04
 */
public interface SysUserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysUser queryById(Long id);

    /**
     * 分页查询
     *
     * @param sysUser 筛选条件
     * @return 查询结果
     */
    PageResponse<SysUser> queryByPage(SysUserDto sysUser);

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    Integer insert(UserDto sysUser);

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    SysUser update(SysUser sysUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);


    Map<Integer,SysUser> queryByList(List<Integer> k);
}
