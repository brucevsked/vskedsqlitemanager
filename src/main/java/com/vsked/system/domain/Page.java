package com.vsked.system.domain;

import java.util.LinkedList;
import java.util.List;

public class Page<T> {

    /**
     * current page
     */
    private PageIndex currentPageIndex;
    /**
     * every page has records count
     */
    private PageSize pageSize;

    /**
     * all records count
     */
    private PageTotalElementsNumber pageTotalElementsNumber;

    /**
     * current page data
     */
    private List<T> data=new LinkedList<>();

    public Page(PageIndex currentPageIndex, PageSize pageSize) {
        this.currentPageIndex = currentPageIndex;
        this.pageSize = pageSize;
    }

    public PageIndex getCurrentPageIndex() {
        return currentPageIndex;
    }

    public PageSize getPageSize() {
        return pageSize;
    }

    public PageTotalElementsNumber getPageTotalElementsNumber() {
        return pageTotalElementsNumber;
    }

    public List<T> getData() {
        return data;
    }

    public void setPageTotalElementsNumber(PageTotalElementsNumber pageTotalElementsNumber) {
        this.pageTotalElementsNumber = pageTotalElementsNumber;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String toString() {
        return "Page{" +
                "currentPageIndex=" + currentPageIndex +
                ", pageSize=" + pageSize +
                ", pageTotalElementsNumber=" + pageTotalElementsNumber +
                ", data=" + data +
                '}';
    }
}
