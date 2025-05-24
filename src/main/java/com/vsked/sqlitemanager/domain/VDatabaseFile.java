package com.vsked.sqlitemanager.domain;

import java.io.File;
import java.util.Objects;

/**
 * 表示一个 SQLite 数据库文件对象。
 * 封装了 File 对象，并使用 VDatabaseFileName 来验证文件名格式。
 */
public class VDatabaseFile {

    private final File databaseFile;
    private final VDatabaseFileName databaseFileName;

    public VDatabaseFile(File databaseFile) {
        if (databaseFile == null) {
            throw new IllegalArgumentException("Database file cannot be null.");
        }

        // 获取文件名部分并进行验证
        String fileName = databaseFile.getName();
        this.databaseFileName = new VDatabaseFileName(fileName);

        // 保存文件对象副本，防止外部修改
        this.databaseFile = new File(databaseFile.getAbsolutePath());
    }

    /**
     * 获取数据库文件的原始 File 对象（不可变副本）
     */
    public File getDatabaseFile() {
        return new File(databaseFile.getAbsolutePath()); // 返回副本，防止外部修改
    }

    /**
     * 获取数据库文件名（不带路径），已通过 VDatabaseFileName 验证
     */
    public String getFileName() {
        return databaseFileName.getFileName();
    }

    /**
     * 获取不带扩展名的文件名
     */
    public String getBaseName() {
        return databaseFileName.getBaseName();
    }

    /**
     * 获取文件扩展名（包括点号）
     */
    public String getExtension() {
        return databaseFileName.getExtension();
    }

    /**
     * 获取数据库文件路径（绝对路径）
     */
    public String getFilePath() {
        return databaseFile.getAbsolutePath();
    }

    /**
     * 判断数据库文件是否存在
     */
    public boolean exists() {
        return databaseFile.exists();
    }

    /**
     * 判断是否是有效的 SQLite 文件（以 .db 或 .sqlite 结尾）
     */
    public boolean isValidSQLiteFile() {
        return true; // 已经由 VDatabaseFileName 验证过
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VDatabaseFile)) return false;
        VDatabaseFile that = (VDatabaseFile) o;
        return Objects.equals(databaseFile, that.databaseFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(databaseFile);
    }

    @Override
    public String toString() {
        return "VDatabaseFile{" +
                "filePath='" + databaseFile.getAbsolutePath() + '\'' +
                ", fileName=" + databaseFileName +
                '}';
    }
}
