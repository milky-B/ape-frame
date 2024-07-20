package com.airport.ape.user.entity.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AuthReportDto {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("鉴定机构id")
    private Long authInstitutionId;

    @ApiModelProperty("报告编号")
    private String reportNumber;

    @ApiModelProperty("鉴定报告编码")
    private String unionId;

    @ApiModelProperty("有效时间类别")
    private Integer effectiveType;

    @ApiModelProperty("有效时间起始")
    private Date effectiveStart;

    @ApiModelProperty("有效时间结束")
    private Date effectiveEnd;

    @ApiModelProperty("厂商id")
    private Long manufacturerId;

    @ApiModelProperty("项目编号")
    private String itemNo;

    @ApiModelProperty("签发日期")
    private String issueDate;

    @ApiModelProperty("鉴别日期")
    private Date identificationDate;

    @ApiModelProperty("审核状态")
    private Integer state;

    @ApiModelProperty("上传时间")
    private Date uploadTime;

    @ApiModelProperty("鉴别目的")
    private String identificationPurpose;

    @ApiModelProperty("鉴别依据")
    private String identificationCriteria;

    @ApiModelProperty("鉴定结论")
    private String conclusions;

    @ApiModelProperty("unid")
    private String unid;

    @ApiModelProperty("运输专用名称")
    private String properShippingName;

    @ApiModelProperty("类或项")
    private Integer clazz;

    @ApiModelProperty("包装等级")
    private Integer packingGroup;

    @ApiModelProperty("客货机")
    private Integer passengerCargoAircraft;

    @ApiModelProperty("仅限货机")
    private Integer cargoAircraftOnly;

    @ApiModelProperty("注意事项")
    private String remarks;

    @ApiModelProperty("所属运单编码")
    private String waybillCode;

    @ApiModelProperty("电池型号")
    private String model;


    @ApiModelProperty("是否覆盖")
    private Integer override;

    @ApiModelProperty("重量")
    private String weight;

    @ApiModelProperty("委托单位")
    private String entrustedUnit;

    @ApiModelProperty("生产单位")
    private String productionUnit;

    @ApiModelProperty("测试单位")
    private String testUnit;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("样品英文名称")
    private String sampleNameEn;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("品牌英文")
    private String brandEn;

    @ApiModelProperty("型号")
    private String type;

    @ApiModelProperty("型号英文")
    private String typeEn;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("描述英文")
    private String descriptionEn;

    @ApiModelProperty("锂含量")
    private String liContent;

    @ApiModelProperty("锂含量英文")
    private String liContentEn;

    @ApiModelProperty("质量")
    private String mass;

    @ApiModelProperty("质量英文")
    private String massEn;

    @ApiModelProperty("外观")
    private String appearance;

    @ApiModelProperty("外观英文")
    private String appearanceEn;

    @ApiModelProperty("38.33.3(f)")
    private String thirtyEightF;

    @ApiModelProperty("38.33.3(g)")
    private String thirtyEightG;

    @ApiModelProperty("报告结论")
    private String reportConclusion;
}
