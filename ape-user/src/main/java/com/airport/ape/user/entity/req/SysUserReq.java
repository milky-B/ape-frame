package com.airport.ape.user.entity.req;

import com.airport.ape.web.entity.PageRequest;
import lombok.Data;

import java.util.Date;

/**
 * (SysUser)实体类
 *
 * @author makejava
 * @since 2023-11-08 20:06:03
 */
@Data
public class SysUserReq extends PageRequest {
    
    private Long id;
    
    private String name;
    
    private Integer age;
    
    private String createBy;
    
    private Date createTime;
    
    private String updateBy;
    
    private Date updateTime;
    
    private Integer deleteFlag;
    
    private Integer version;


}

