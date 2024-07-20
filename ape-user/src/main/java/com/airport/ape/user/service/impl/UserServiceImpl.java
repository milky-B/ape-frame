package com.airport.ape.user.service.impl;

import com.airport.ape.mybatis.entity.PageResult;
import com.airport.ape.user.convert.MyConvert;
import com.airport.ape.user.entity.dto.UserWithWorkOrderDto;
import com.airport.ape.user.entity.po.SysUser;
import com.airport.ape.user.mapper.UserMapper;
import com.airport.ape.user.entity.dto.UserDto;
import com.airport.ape.user.entity.po.UserPo;
import com.airport.ape.user.mapper.WarehouseWorkOrderMapper;
import com.airport.ape.user.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MyConvert myConvert;
    @Override
    public int addUser(UserDto userDto) {
        UserPo userPo = myConvert.toUserPo(userDto);
        System.out.println(userPo);
        int i = userMapper.insert(userPo);
        return i;
    }

    @Override
    public int delUser(Integer id) {
        return userMapper.deleteById(id);
    }

    @Override
    public PageResult<UserPo> queryUserList(UserDto userDto) {
        Page<UserPo> userPoIPage = new Page<>(userDto.getPageIndex(),userDto.getSize());
        IPage<UserPo> userPage = userMapper.selectUserPage(userPoIPage);
        PageResult<UserPo> pageResult = new PageResult<>(userPage);
        return pageResult;
    }
    @Override
    public void test() {
        /*List<Long> list = new ArrayList<>();
        for(int i=0;i<1000;i++){
            UserPo user = new UserPo();
            userMapper.insert(user);
            list.add(user.getId());
        }
        list.forEach(System.out::println);*/
        UserPo user = new UserPo();
        userMapper.insert(user);
        System.out.println(user.getId());
    }
    @Autowired
    private WarehouseWorkOrderMapper warehouseWorkOrderMapper;
    @Override
    public List<UserWithWorkOrderDto> userWithWorkOrder(Long id, String start, String end) {
        List<UserWithWorkOrderDto> userWithWorkOrderList = userMapper.selectUserList(id);
        userWithWorkOrderList.forEach(user->{
            Long id1 = user.getId();
            user.setWarehouseWorkOrder(warehouseWorkOrderMapper.selectByUerId(id1,start,end));
        });
        return userWithWorkOrderList;
    }
    public List<UserWithWorkOrderDto> test(Long id, String start, String end){
        List<UserWithWorkOrderDto> userWithWorkOrderDtos = userMapper.queryUserWithWorkOrderBySupplierIdBak(id, start, end);
        return userWithWorkOrderDtos;
    }

    @Override
    public Integer count(UserPo userPo) {
        return userMapper.selectCountByEntity(userPo);
    }
}
