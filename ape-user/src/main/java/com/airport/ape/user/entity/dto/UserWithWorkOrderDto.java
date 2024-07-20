package com.airport.ape.user.entity.dto;

import com.airport.ape.user.entity.po.WarehouseWorkOrder;
import lombok.Data;

import java.util.List;

@Data
public class UserWithWorkOrderDto {
    private Long id;
    private String name;
    private List<WarehouseWorkOrder> warehouseWorkOrder;

    @Override
    public String toString() {
        return "UserWithWorkOrderDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", warehouseWorkOrder=" + warehouseWorkOrder +
                '}';
    }
}
