//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.airport.ape.user.entity.po;

import java.util.Date;
public class WarehouseWorkOrder {
    private Long id;
    private Long tenantId;
    private Long mainOrderId;
    private String batchNumber;
    private String mawbId;
    private String hawbId;
    private String transactionId;
    private Long warehouseId;
    private Long warehouseAreaId;
    private Long warehouseSortingAreaId;
    private Long goodsBottomId;
    private Long warehousePositionId;
    private Long oldPositionId;
    private String rfidClientDeviceId;
    private String specificGoodsCode;
    private Long outsourcedUserId;
    private Integer workState;
    private Integer workType;
    private Date goodsLoadingTime;
    private Date accessEntranceTime;
    private Date goodsOperateTime;
    private Date accessExitTime;
    private Date finishTime;
    private Long creator;
    private Date createTime;
    private Date modifyTime;

    public WarehouseWorkOrder() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getMainOrderId() {
        return this.mainOrderId;
    }

    public void setMainOrderId(Long mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public String getBatchNumber() {
        return this.batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getMawbId() {
        return this.mawbId;
    }

    public void setMawbId(String mawbId) {
        this.mawbId = mawbId == null ? null : mawbId.trim();
    }

    public String getHawbId() {
        return this.hawbId;
    }

    public void setHawbId(String hawbId) {
        this.hawbId = hawbId == null ? null : hawbId.trim();
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : transactionId.trim();
    }

    public Long getWarehouseId() {
        return this.warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getWarehouseAreaId() {
        return this.warehouseAreaId;
    }

    public void setWarehouseAreaId(Long warehouseAreaId) {
        this.warehouseAreaId = warehouseAreaId;
    }

    public Long getWarehouseSortingAreaId() {
        return this.warehouseSortingAreaId;
    }

    public void setWarehouseSortingAreaId(Long warehouseSortingAreaId) {
        this.warehouseSortingAreaId = warehouseSortingAreaId;
    }

    public Long getGoodsBottomId() {
        return this.goodsBottomId;
    }

    public void setGoodsBottomId(Long goodsBottomId) {
        this.goodsBottomId = goodsBottomId;
    }

    public Long getWarehousePositionId() {
        return this.warehousePositionId;
    }

    public void setWarehousePositionId(Long warehousePositionId) {
        this.warehousePositionId = warehousePositionId;
    }

    public Long getOldPositionId() {
        return this.oldPositionId;
    }

    public void setOldPositionId(Long oldPositionId) {
        this.oldPositionId = oldPositionId;
    }

    public String getRfidClientDeviceId() {
        return this.rfidClientDeviceId;
    }

    public void setRfidClientDeviceId(String rfidClientDeviceId) {
        this.rfidClientDeviceId = rfidClientDeviceId == null ? null : rfidClientDeviceId.trim();
    }

    public Long getOutsourcedUserId() {
        return this.outsourcedUserId;
    }

    public void setOutsourcedUserId(Long outsourcedUserId) {
        this.outsourcedUserId = outsourcedUserId;
    }

    public String getSpecificGoodsCode() {
        return this.specificGoodsCode;
    }

    public void setSpecificGoodsCode(String specificGoodsCode) {
        this.specificGoodsCode = specificGoodsCode;
    }

    public Integer getWorkState() {
        return this.workState;
    }

    public void setWorkState(Integer workState) {
        this.workState = workState;
    }

    public Integer getWorkType() {
        return this.workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

    public Date getGoodsLoadingTime() {
        return this.goodsLoadingTime;
    }

    public void setGoodsLoadingTime(Date goodsLoadingTime) {
        this.goodsLoadingTime = goodsLoadingTime;
    }

    public Date getAccessEntranceTime() {
        return this.accessEntranceTime;
    }

    public void setAccessEntranceTime(Date accessEntranceTime) {
        this.accessEntranceTime = accessEntranceTime;
    }

    public Date getGoodsOperateTime() {
        return this.goodsOperateTime;
    }

    public void setGoodsOperateTime(Date goodsOperateTime) {
        this.goodsOperateTime = goodsOperateTime;
    }

    public Date getAccessExitTime() {
        return this.accessExitTime;
    }

    public void setAccessExitTime(Date accessExitTime) {
        this.accessExitTime = accessExitTime;
    }

    public Date getFinishTime() {
        return this.finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Long getCreator() {
        return this.creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        return "WarehouseWorkOrder{" +
                "id=" + id +
                ", workType=" + workType +
                ", goodsOperateTime=" + goodsOperateTime +
                ", finishTime=" + finishTime +
                '}';
    }
}
