package com.airport.ape.user.backUp;

import com.airport.ape.user.entity.po.SysUser;
import com.airport.ape.user.mapper.SysUserBakMapper;
import com.airport.ape.user.mapper.SysUserMapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SysUserForwardHandler extends AbstractBackUpDataHandler<SysUser, SysUserBackUpDataRule> {

    /**
     * 归档规则，可以配合nacos动态配置
     */
    private String sysUserBackupRuleStr="{\n" +
            "  \"beginId\": 28,\n" +
            "  \"querySize\": 5\n" +
            "}";

    /**
     * 佣金系数归档停止flag，配合nacos
     */
    private Boolean stopFlag=false;

    @Resource
    private SysUserMapper sysUserDao;

    @Resource
    private SysUserBakMapper sysUserBakDao;

    @Override
    public Boolean needStop() {
        return stopFlag;
    }

    @Override
    public BackUpDataSceneEnum getScene() {
        return BackUpDataSceneEnum.USER_FORWARD;
    }

    @Override
    public List<SysUser> queryData(SysUserBackUpDataRule rule) {
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.ge("id",rule.getBeginId());
        sysUserQueryWrapper.lt("id",rule.getEndId());
        List<SysUser> sysUserList = sysUserDao.selectList(sysUserQueryWrapper);
        return sysUserList;
    }

    @Override
    public void insertData(List<SysUser> sysUserList) {
        sysUserBakDao.insertBatch(sysUserList);
    }

    @Override
    public void deleteData(List<SysUser> dataList) {
        List<Long> ids = dataList.stream().map(SysUser::getId).collect(Collectors.toList());
        sysUserDao.deleteBatchIds(ids);
    }

    @Override
    public SysUserBackUpDataRule getRule() {
        SysUserBackUpDataRule backUpRule = JSON.parseObject(sysUserBackupRuleStr, SysUserBackUpDataRule.class);
        Preconditions.checkNotNull(backUpRule, "归档规则不能为空！");
        Preconditions.checkNotNull(backUpRule.getQuerySize(), "查询数量不能为空！");
        Preconditions.checkNotNull(backUpRule.getBeginId(), "beginId不能为空！");
        Long endId = backUpRule.getBeginId() + backUpRule.getQuerySize();
        backUpRule.setEndId(endId);
        return backUpRule;
    }

    @Override
    public SysUserBackUpDataRule changeOffSet(SysUserBackUpDataRule backupDataRule) {
        backupDataRule.setBeginId(backupDataRule.getEndId());
        Long endId = backupDataRule.getBeginId() + backupDataRule.getQuerySize();
        backupDataRule.setEndId(endId);
        return backupDataRule;
    }

}