package com.vsked.system.domain;

/**
 * page index ,current page
 */
public class PageIndex {

    /**
     * current page first records start index
     */
    private Integer index;

    public PageIndex(Integer index) {
        if(index==null){
            index=0;
        }
        if(index <=0){
            index=0;
        }
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public String toString() {
        return "" + index;
    }
}
