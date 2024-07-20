package com.airport.ape.user.controller;

import com.airport.ape.user.backUp.BackUpDataSceneEnum;
import com.airport.ape.user.backUp.BackUpService;
import com.airport.ape.web.entity.PageResponse;
import com.airport.ape.web.entity.Result;
import com.airport.ape.user.entity.dto.SysUserDto;
import com.airport.ape.user.entity.dto.UserDto;
import com.airport.ape.user.entity.po.SysUser;
import com.airport.ape.user.entity.req.SysUserReq;
import com.airport.ape.user.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * (SysUser)表控制层
 *
 * @author makejava
 * @since 2023-11-08 20:06:02
 */
@RestController
@RequestMapping("sysUser")
public class SysUserController {
    /**
     * 服务对象
     */
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private BackUpService backUpService;

    /**
     * 分页查询
     *
     * @param sysUserReq 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public Result<PageResponse<SysUser>> queryByPage(SysUserReq sysUserReq) {
        SysUserDto sysUserDto = new SysUserDto();
        BeanUtils.copyProperties(sysUserReq,sysUserDto);
        sysUserDto.setStart((sysUserReq.getCurrent()-1)*sysUserReq.getSize());
        PageResponse<SysUser> sysUserPageResponse = this.sysUserService.queryByPage(sysUserDto);
        return Result.success(sysUserPageResponse);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<SysUser> queryById(@PathVariable("id") Long id) {
        SysUser sysUser = this.sysUserService.queryById(id);
        System.out.println(sysUser);
        return ResponseEntity.ok(sysUser);
    }

    /**
     * 新增数据
     *
     * @param userDto 实体
     * @return 新增结果
     */
    @PostMapping
    public Result<Integer> add(@RequestBody UserDto userDto) {
        return Result.success(this.sysUserService.insert(userDto));
    }

    /**
     * 编辑数据
     *
     * @param sysUser 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<SysUser> edit(SysUser sysUser) {
        return ResponseEntity.ok(this.sysUserService.update(sysUser));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.sysUserService.deleteById(id));
    }
/*    @Autowired
    private CacheUtil cacheUtil;

    @GetMapping("/guava")
    public ResponseEntity<Map> queryById(){
        ArrayList arrayList = new ArrayList();
        arrayList.add(30);
        arrayList.add(31);
        arrayList.add(32);
        arrayList.add(33);
        arrayList.add(34);
        Map result = cacheUtil.getResult(arrayList, "localSysUser", "guava", SysUser.class, (k) -> {
            Map<Integer, SysUser> map = sysUserService.queryByList((List<Integer>) k);
            return map;
        });
        return ResponseEntity.ok(result);
    }*/

    @GetMapping("backUp")
    public Result backUpSysUser(){
        backUpService.backUp(BackUpDataSceneEnum.USER_FORWARD);
        return Result.success();
    }
}

