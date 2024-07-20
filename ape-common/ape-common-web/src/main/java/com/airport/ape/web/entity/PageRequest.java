package com.airport.ape.web.entity;

public class PageRequest {
    private Long size = 10L;
    private Long current = 1L;
    public void setSize(){
        if(size!=null&&this.size!=size){
            this.size = size<1||size>Integer.MAX_VALUE?10:size;
        }
    }
    public void setCurrent(Long current){
        if(current!=null&&this.current!=current){
            this.current = current<0||current>Integer.MAX_VALUE?1:current;
        }
    }
    public Long getSize(){
        return size<1||size>Integer.MAX_VALUE?10:size;
    }
    public Long getCurrent(){
        return current<1||current>Integer.MAX_VALUE?1:current;
    }
}
