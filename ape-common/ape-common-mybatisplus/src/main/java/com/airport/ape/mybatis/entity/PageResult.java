package com.airport.ape.mybatis.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private long total;
    private long size;
    private long current;
    private long pages;
    private List<T> records;

    public PageResult(IPage<T> iPage){
        total=iPage.getTotal();
        size=iPage.getSize();
        current=iPage.getCurrent();
        pages=iPage.getPages();
        records=iPage.getRecords();
    }
}
