package com.vsked.system.domain;

import java.util.Arrays;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

public enum PermissionContent {

    READ("读"),
    WRITE("写");

    private final String description;

    PermissionContent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据描述获取权限类型（忽略大小写）
     */
    public static Optional<PermissionContent> fromDescription(String description) {
        if (description == null || description.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(p -> p.getDescription().equalsIgnoreCase(description))
                .findFirst();
    }

    /**
     * 获取所有权限描述
     */
    public static Set<String> getAllDescriptions() {
        return Arrays.stream(values())
                .map(PermissionContent::getDescription)
                .sorted()
                .collect(Collectors.toSet());
    }

    /**
     * 获取所有权限名称（枚举名）
     */
    public static Set<String> getAllNames() {
        return Arrays.stream(values())
                .map(Enum::name)
                .sorted()
                .collect(Collectors.toSet());
    }

    /**
     * 转换为字符串数组（权限描述）
     */
    public static String[] getDescriptionArray() {
        return getAllDescriptions().toArray(new String[0]);
    }

    /**
     * 转换为字符串数组（权限名称）
     */
    public static String[] getNameArray() {
        return getAllNames().toArray(new String[0]);
    }
}