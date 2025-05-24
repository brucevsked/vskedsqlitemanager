package com.vsked.sqlitemanager.domain;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

/**
 * 分页对象，用于封装分页请求和返回结果。
 *
 * @param <T> 数据类型
 */
public class VPage<T> {

    // 当前页码（从 0 开始）
    private final VPageIndex currentPageIndex;

    // 每页记录数
    private final VPageSize pageSize;

    // 总记录数
    private VPageTotalElementsNumber totalElementsNumber;

    // 当前页的数据集合
    private List<T> data;

    // 查询条件列表（可选）
    private final List<VQueryCondition> conditions;

    public VPage(VPageIndex currentPageIndex, VPageSize pageSize) {
        this(currentPageIndex, pageSize, new LinkedList<>(), new LinkedList<>());
    }

    public VPage(VPageIndex currentPageIndex, VPageSize pageSize, List<T> data) {
        this(currentPageIndex, pageSize, data, new LinkedList<>());
    }

    public VPage(VPageIndex currentPageIndex, VPageSize pageSize, List<T> data, List<VQueryCondition> conditions) {
        this.currentPageIndex = Objects.requireNonNull(currentPageIndex, "currentPageIndex cannot be null");
        this.pageSize = Objects.requireNonNull(pageSize, "pageSize cannot be null");
        this.data = Collections.unmodifiableList(data != null ? data : new LinkedList<>());
        this.conditions = Collections.unmodifiableList(conditions != null ? conditions : new LinkedList<>());
    }

    // 获取当前页码（从 0 开始）
    public VPageIndex getCurrentPageIndex() {
        return currentPageIndex;
    }

    // 获取每页大小
    public VPageSize getPageSize() {
        return pageSize;
    }

    // 获取总记录数
    public VPageTotalElementsNumber getTotalElementsNumber() {
        return totalElementsNumber;
    }

    // 设置总记录数
    public void setTotalElementsNumber(VPageTotalElementsNumber totalElementsNumber) {
        this.totalElementsNumber = totalElementsNumber;
    }

    // 获取当前页数据（只读）
    public List<T> getData() {
        return data;
    }

    // 获取查询条件（只读）
    public List<VQueryCondition> getConditions() {
        return conditions;
    }

    // 是否有上一页
    public boolean hasPrevious() {
        return currentPageIndex.getPageIndex() > 0;
    }

    // 是否是首页
    public boolean isFirst() {
        return !hasPrevious();
    }

    // 是否是尾页
    public boolean isLast() {
        if (totalElementsNumber == null) {
            return false; // 未知总数时无法判断
        }
        int totalPages = getTotalPages();
        return currentPageIndex.getPageIndex() >= totalPages - 1;
    }

    // 获取总页数
    public int getTotalPages() {
        if (totalElementsNumber == null || pageSize.getPageSize() <= 0) {
            return 0;
        }
        long total = totalElementsNumber.getTotalElementsNumber();
        return (int) Math.ceil((double) total / pageSize.getPageSize());
    }

    // 判断当前页是否有数据
    public boolean hasContent() {
        return !data.isEmpty();
    }

    // 获取当前页号（从 1 开始）
    public int getPageNumber() {
        return currentPageIndex.getPageIndex() + 1;
    }

    // 获取当前页第一个元素索引（从 0 开始）
    public int getStartItemIndex() {
        return hasContent() ? currentPageIndex.getPageIndex() * pageSize.getPageSize() : 0;
    }

    // 获取当前页最后一个元素索引（从 0 开始）
    public int getEndItemIndex() {
        if (!hasContent()) return 0;
        int start = getStartItemIndex();
        return (int) (Math.min(start + data.size(), getTotalElementsNumber().getTotalElementsNumber()) - 1);
    }

    @Override
    public String toString() {
        return "VPage{" +
                "pageIndex=" + currentPageIndex.getPageIndex() +
                ", pageSize=" + pageSize.getPageSize() +
                ", totalElements=" + (totalElementsNumber != null ? totalElementsNumber.getTotalElementsNumber() : "unknown") +
                ", dataCount=" + data.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VPage<?> vPage)) return false;
        return Objects.equals(currentPageIndex, vPage.currentPageIndex)
                && Objects.equals(pageSize, vPage.pageSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPageIndex, pageSize);
    }

    /**
     * 设置当前页的数据集合（会自动转为只读列表）
     */
    public void setData(List<T> data) {
        this.data = Collections.unmodifiableList(data != null ? new LinkedList<>(data) : new LinkedList<>());
    }

}
