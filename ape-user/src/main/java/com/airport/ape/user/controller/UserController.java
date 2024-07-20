package com.airport.ape.user.controller;

import com.airport.ape.mybatis.entity.PageResult;
import com.airport.ape.user.entity.dto.UserWithWorkOrderDto;
import com.airport.ape.user.entity.po.UserPo;
import com.airport.ape.user.entity.po.WarehouseWorkOrder;
import com.airport.ape.user.excel.UserExcelExport;
import com.airport.ape.user.mapper.UserMapper;
import com.airport.ape.web.entity.Result;
import com.airport.ape.user.convert.MyConvert;
import com.airport.ape.user.entity.dto.UserDto;
import com.airport.ape.user.entity.req.UserListReq;
import com.airport.ape.user.entity.req.UserReq;
import com.airport.ape.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/user")
@Api("用户管理")
public class UserController {
    @Autowired
    private UserService userService;
    @ApiOperation("添加用户")
    @PostMapping
    public Result add(@RequestBody @ApiParam("用户信息") UserReq userReq){
        UserDto userDto = MyConvert.INSTANCE.toUserDto(userReq);
        return Result.success(userService.addUser(userDto));
    }
    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Result del(@PathVariable Integer id){
        return Result.success(userService.delUser(id));
    }
    @ApiOperation("查看用户列表")
    @GetMapping("/userList")
    public Result list(UserListReq userListReq){
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userListReq,userDto);
        return Result.success(userService.queryUserList(userDto));
    }

    @GetMapping("snow")
    public void snowTest(){
        userService.test();
    }

    @Autowired
    private UserMapper userMapper;
    @GetMapping("test")
    public List<UserWithWorkOrderDto> queryUserWithWorkOrderBySupplierIdTest(@RequestParam Long id,@RequestParam String start ,@RequestParam String end){
        List<UserWithWorkOrderDto> userWithWorkOrderDto = userService.userWithWorkOrder(id,start,end);
        userWithWorkOrderDto.forEach(dto->{
            System.out.println(dto.getId());
            System.out.println(dto.getName());
            List<WarehouseWorkOrder> warehouseWorkOrder = dto.getWarehouseWorkOrder();
            warehouseWorkOrder.forEach(System.out::println);
        });

        List<UserWithWorkOrderDto> test = userService.test(id, start, end);
        test.forEach(v->{
            System.out.println(v);
        });
        return userWithWorkOrderDto;
    }
    @Autowired
    private UserExcelExport userExcelExport;
    @GetMapping("exportData")
    public void exportData(){
        userExcelExport.bigDataExportExcel("bigDataExport",new HashMap<>());
    }
}
