package com.vsked.sqlitemanager.domain;

/**
 * pageIndex,also current page number
 */
public class PageIndex {
    /**
     * start index at current page
     */
    private Integer index;

    public PageIndex(Integer index) {
        if(index==null){
            index= 0;
        }
        if(index <=0){
            index= 0;
        }
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

}
