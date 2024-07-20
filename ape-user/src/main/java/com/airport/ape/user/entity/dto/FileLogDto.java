package com.airport.ape.user.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class FileLogDto {
    @ApiModelProperty("存储路径")
    private String path;
    @ApiModelProperty("运单号")
    private Long waybillId;
}