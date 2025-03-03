package com.vsked.sqlitemanager.domain;

import java.util.LinkedList;
import java.util.List;

public class Page<T> {
	   /**
     * current page
     */
    private PageIndex currentPageIndex;
    /**
     * records in per page
     */
    private PageSize pageSize;

    /**
     * all records number
     */
    private PageTotalElementsNumber pageTotalElementsNumber;

    /**
     * current page data
     */
    private List<T> data=new LinkedList<>();
    
    /**
     * query condition value
     */
    List<QueryCondition> conditions = new LinkedList<QueryCondition>();

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
    
    public List<QueryCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}

}
