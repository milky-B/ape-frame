package com.airport.ape.user.entity.req;

import lombok.Data;

@Data
public class UserListReq {
    private Integer pageIndex;
    private Integer size;
}
