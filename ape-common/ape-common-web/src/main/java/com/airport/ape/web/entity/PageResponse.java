package com.airport.ape.web.entity;

import lombok.Getter;

import java.util.Collections;
import java.util.List;
@Getter
public class PageResponse<T> {
    private Long total;
    private Long pages;
    private Long current = 1L;
    private Long size = 10L;
    private List<T> data = Collections.emptyList();
    private Long start = 1L;
    private Long end = 0L;
    public PageResponse(){}

    public void setTotal(Long total) {
        this.total = total;
        if(total==-1){
            this.current = 1L;
            return;
        }
        this.pages = total/(this.size)+(total%this.size==0?0:1);
        this.start = (this.current-1)*this.size+1;
        this.end = (this.start+this.size)>total?total:this.start+this.size-1;
    }
    public void setData(List<T> data){
        this.data = data;
        if(data!=null&&data.size()>0){
            this.start = (this.current-1)*this.size+1;
            this.end = this.start + data.size()-1;
        }
    }
    public void setCurrent(Long current){
        if(current!=null&&this.current!=current){
            this.current = current<0||current>Integer.MAX_VALUE?1:current;
            if(this.total!=null){
                setTotal(total);
            }
        }
    }
    public void setSize(Long size){
        if(size!=null&&this.size!=size){
            this.size = size<1||size>Integer.MAX_VALUE?10:size;
            if(this.total!=null){
                setTotal(total);
            }
        }
    }
}
