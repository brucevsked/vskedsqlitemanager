package com.vsked.sqlitemanager.domain;

import java.util.Objects;

/**
 * 表示分页数据的总记录数。
 */
public class VPageTotalElementsNumber {

    private final long totalElementsNumber;

    public VPageTotalElementsNumber(Long total) {
        if (total == null || total < 0) {
            this.totalElementsNumber = 0;
        } else {
            this.totalElementsNumber = total;
        }
    }

    /**
     * 获取总记录数
     */
    public long getTotalElementsNumber() {
        return totalElementsNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VPageTotalElementsNumber)) return false;
        VPageTotalElementsNumber that = (VPageTotalElementsNumber) o;
        return totalElementsNumber == that.totalElementsNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalElementsNumber);
    }

    @Override
    public String toString() {
        return "VPageTotalElementsNumber{" +
                "totalElementsNumber=" + totalElementsNumber +
                '}';
    }
}
