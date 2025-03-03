package com.vsked.sqlitemanager.domain;

/**
 * total records number,also all records number
 */
public class PageTotalElementsNumber {

    private Long total;

    public PageTotalElementsNumber(Long total) {
        if(total==null){
            total= 0L;
        }
        this.total = total;
    }

    public Long getTotal() {
        return total;
    }

}
