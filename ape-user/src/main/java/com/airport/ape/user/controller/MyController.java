package com.airport.ape.user.controller;

import com.airport.ape.swagger.entity.SwaggerInfo;
import com.airport.ape.tool.ExportWordUtil;
import com.airport.ape.tool.IpUtil;

import com.airport.ape.web.entity.Result;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@RestController
@ConditionalOnProperty(value = "test.controller.conditional", havingValue = "true", matchIfMissing = true)
public class MyController {
    @Autowired
    private SwaggerInfo swaggerInfo;
    @Resource(name = "mailThreadPool")
    private ThreadPoolExecutor poolExecutor;

    @Autowired
    /*private CategoryCache categoryCache;*/
    @GetMapping("test")
    public String test(){
        return swaggerInfo.toString();
    }
    /*@DeleteMapping("clear")
    public void clear(){
        categoryCache.clearCache();
    }*/
/*    @GetMapping("export")
    public Result export(){
        // 准备测试数据
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", "John Doe");
        dataMap.put("age", 30);
        dataMap.put("address", "123 Main Street");

        // 指定模板文件名
        String templateName = "/template/word/test.html";

        // 设置标题
        String title = "TestDocument";

        // 调用导出方法
        try {
            ExportWordUtil.exportWord(dataMap, title, templateName);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }*/
    @GetMapping("ip")
    public Map getIp(HttpServletRequest httpServletRequest){
        Map<String,String> map = new HashMap<>();
        map.put("ip",IpUtil.getIp(httpServletRequest));
        map.put("user-agent",httpServletRequest.getHeader("User-Agent"));
        return map;
    }

}
