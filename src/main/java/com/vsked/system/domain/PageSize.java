package com.vsked.system.domain;

/**
 * every page has records count
 */
public class PageSize {

    private Integer size;

    public PageSize(Integer size) {
        if(size==null){
            size=10;
        }
        if(size <=0){
            size=10;
        }
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

    public String toString() {
        return "" +size;
    }
}
