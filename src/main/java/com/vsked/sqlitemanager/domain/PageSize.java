package com.vsked.sqlitemanager.domain;

/**
 * records in per page
 */
public class PageSize {

    private Integer size;

    public PageSize(Integer size) {
        if(size==null){
            size= 10;
        }
        if(size <=0){
            size= 10;
        }
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

}
