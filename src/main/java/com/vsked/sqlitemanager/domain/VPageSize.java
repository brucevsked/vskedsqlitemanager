package com.vsked.sqlitemanager.domain;

import java.util.Objects;

/**
 * 表示每页显示的数据条数。
 */
public class VPageSize {

    private final int pageSize;

    public VPageSize(Integer size) {
        if (size == null || size <= 0) {
            this.pageSize = 10; // 默认每页大小为 10
        } else {
            this.pageSize = size;
        }
    }

    /**
     * 获取每页记录数量
     */
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VPageSize)) return false;
        VPageSize that = (VPageSize) o;
        return pageSize == that.pageSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageSize);
    }

    @Override
    public String toString() {
        return "VPageSize{" +
                "pageSize=" + pageSize +
                '}';
    }
}
