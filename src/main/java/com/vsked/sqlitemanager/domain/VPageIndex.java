package com.vsked.sqlitemanager.domain;

import java.util.Objects;

/**
 * 表示分页中的当前页码（从 0 开始）。
 */
public class VPageIndex {

    private final int pageIndex;

    public VPageIndex(Integer index) {
        if (index == null || index < 0) {
            this.pageIndex = 0;
        } else {
            this.pageIndex = index;
        }
    }

    /**
     * 获取当前页码（从 0 开始）
     */
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VPageIndex)) return false;
        VPageIndex that = (VPageIndex) o;
        return pageIndex == that.pageIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageIndex);
    }

    @Override
    public String toString() {
        return "VPageIndex{" +
                "pageIndex=" + pageIndex +
                '}';
    }
}
