package com.airport.ape.user.excel;

import com.airport.ape.mybatis.entity.PageResult;
import com.airport.ape.tool.excel.BaseEasyExcelExport;
import com.airport.ape.user.entity.dto.UserDto;
import com.airport.ape.user.entity.po.UserPo;
import com.airport.ape.user.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component
public class UserExcelExport extends BaseEasyExcelExport<UserPo> {

    @Autowired
    private UserService userService;

    public void bigDataExportExcel(String fileName, Map<String, Object> queryCondition) {
        this.exportExcel(fileName, queryCondition);
    }

    @Override
    protected List<List<String>> getExcelHead() {
        List<List<String>> head = new ArrayList<>();
        head.add(Collections.singletonList("主键"));
        head.add(Collections.singletonList("姓名"));
        head.add(Collections.singletonList("密码"));
        return head;
    }

    @Override
    protected Integer eachSheetTotalCount() {
        return 10;
    }

    @Override
    protected Integer eachTimesWriteSheetTotalCount() {
        return 2 ;
    }

    @Override
    protected void buildDataList(List<List<String>> resultList, Map<String, Object> queryCondition, Integer pageNo, Integer pageSize) {
        UserDto userDto = new UserDto();
        userDto.setPageIndex(pageNo);
        userDto.setSize(pageSize);
        if(!CollectionUtils.isEmpty(queryCondition)){
            userDto.setName((String) queryCondition.get("name"));
            userDto.setAge((Integer) queryCondition.get("age"));
        }

        PageResult<UserPo> userPoPageResult = userService.queryUserList(userDto);
        for(UserPo user:userPoPageResult.getRecords()){
            resultList.add(Arrays.asList(user.getId().toString(),user.getName(),user.getAge().toString()));
        }
    }

    @Override
    protected Integer dataTotalCount(Map<String, Object> conditions){
        UserPo userPo = new UserPo();
        for(Map.Entry<String,Object> entry:conditions.entrySet()){
            try {
                Method declaredMethod = userPo.getClass().getDeclaredMethod("set" + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1), entry.getClass());
                declaredMethod.invoke(userPo,entry.getValue());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return userService.count(userPo);
    }
}
