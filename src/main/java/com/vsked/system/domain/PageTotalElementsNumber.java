package com.vsked.system.domain;

/**
 * all records count
 */
public class PageTotalElementsNumber {

    private Long total;

    public PageTotalElementsNumber(Long total) {
        if(total==null){
            total=0L;
        }
        this.total = total;
    }

    public Long getTotal() {
        return total;
    }

    public String toString() {
        return "" +total;
    }
}
