package com.vsked.sqlitemanager.domain;

import java.util.Objects;

/**
 * 表示一个合法的 SQLite 数据库文件名。
 * 文件名必须非空，并且以 .db 或 .sqlite 结尾。
 */
public class VDatabaseFileName {

    private final String fileName;

    public VDatabaseFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }

        String trimmedName = fileName.trim();
        if (!trimmedName.toLowerCase().endsWith(".db") &&
            !trimmedName.toLowerCase().endsWith(".sqlite")) {
            throw new IllegalArgumentException("File name must end with .db or .sqlite");
        }

        this.fileName = trimmedName;
    }

    /**
     * 获取原始文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 获取不带扩展名的文件名
     */
    public String getBaseName() {
        String name = fileName;
        int dotIndex = name.lastIndexOf('.');
        return (dotIndex == -1) ? name : name.substring(0, dotIndex);
    }

    /**
     * 获取文件扩展名（包括点号）
     */
    public String getExtension() {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VDatabaseFileName)) return false;
        VDatabaseFileName that = (VDatabaseFileName) o;
        return fileName.equalsIgnoreCase(that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName.toLowerCase());
    }

    @Override
    public String toString() {
        return "VDatabaseFileName{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}
