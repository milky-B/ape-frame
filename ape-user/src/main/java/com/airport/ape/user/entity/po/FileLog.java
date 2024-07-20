package com.airport.ape.user.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("file_log")
public class FileLog {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;
    private String path;
    private Long waybill;
    private String pallet;
    private Date createTime;
}
