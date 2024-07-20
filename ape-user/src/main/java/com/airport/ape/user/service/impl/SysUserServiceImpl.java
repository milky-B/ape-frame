package com.airport.ape.user.service.impl;

import com.airport.ape.web.entity.PageResponse;
import com.airport.ape.user.entity.dto.SysUserDto;
import com.airport.ape.user.entity.dto.UserDto;
import com.airport.ape.user.entity.po.SysUser;
import com.airport.ape.user.mapper.SysUserMapper;
import com.airport.ape.user.service.SysUserService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (SysUser)表服务实现类
 *
 * @author makejava
 * @since 2023-11-08 20:06:04
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    @Cacheable(cacheNames = "sysUser",key = "'querySysUserById'+#id")
    public SysUser queryById(Long id) {
        return this.sysUserDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param sysUserDto 筛选条件
     * @return 查询结果
     */
    @Override
    public PageResponse<SysUser> queryByPage(SysUserDto sysUserDto) {
        PageResponse<SysUser> pageResponse = new PageResponse<>();
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDto,sysUser);
        long total = this.sysUserDao.count(sysUser);
        pageResponse.setCurrent(sysUserDto.getCurrent());
        pageResponse.setSize(sysUserDto.getSize());
        pageResponse.setTotal(total);
        List<SysUser> sysUsers = this.sysUserDao.queryAllByLimit(sysUserDto);
        pageResponse.setData(sysUsers);
        return pageResponse;
    }

    /**
     * 新增数据
     *
     * @param userDto 实例对象
     * @return 实例对象
     */
    @Override
    public Integer insert(UserDto userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto,sysUser);
        int i = this.sysUserDao.insert(sysUser);
        return i;
    }

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    public SysUser update(SysUser sysUser) {
        this.sysUserDao.update(sysUser);
        return this.queryById(sysUser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.sysUserDao.deleteById(id) > 0;
    }

    @Override
    public Map<Integer, SysUser> queryByList(List<Integer> ids) {
        Map<Integer,SysUser> map = new HashMap<>();
        for (int id : ids){
            SysUser sysUser = sysUserDao.queryById((long) id);
            if(sysUser!=null){
                map.put(id,sysUser);
            }
        }
        return map;
    }
}
