package com.airport.ape.user.controller;

import com.airport.ape.tool.ExportWordUtil;
import com.airport.ape.user.entity.po.Classes;
import com.airport.ape.user.entity.po.UserPo;
import com.airport.ape.user.mapper.ClassMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("redis test")
@RestController
@Slf4j
public class TestController {
    /*@Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisLockUtil redisLockUtil;
    @GetMapping("/redis/setString")
    @ApiOperation("set string")
    public void test(){
        redisUtil.set("name","lee");
    }

    @GetMapping("/redis/setObject")
    @ApiOperation("set Object")
    public void setObject(){
        UserPo userPo = new UserPo();
        userPo.setName("lee");
        userPo.setId(1001L);
        userPo.setAge(18);
        redisUtil.set("user",userPo);
        UserPo user = (UserPo)redisUtil.get("user");
        log.info("user:"+user.toString());
    }

    @PostMapping("/lock")
    public void lock(){
        redisLockUtil.lock("lock",String.valueOf(Thread.currentThread().getId()),10000L);
        redisLockUtil.lock("",String.valueOf(Thread.currentThread().getId()),10000L);
    }*/
    @GetMapping("logTest")
    public void logTest(){
        Long now  = System.currentTimeMillis();
        for (int i = 0 ;i<100000;i++) {
            log.info("这是第{}条",i);
        }
        long end = System.currentTimeMillis();
        log.info("耗时{}",now-end);
    }
    @GetMapping("export")
    public void testExport() throws Exception{
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("name","lee");
        dataMap.put("auditName","milk");
        ExportWordUtil.exportWord(dataMap,"export","wordExport.flt");
    }
    @Autowired
    private ClassMapper classMapper;
    @GetMapping("classes")
    public void testClassMapper(){
        List<Classes> classes = classMapper.selectClasses();
        classes.forEach(System.out::println);
    }
}
