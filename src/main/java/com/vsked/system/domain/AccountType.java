package com.vsked.system.domain;

import java.util.Arrays;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

public enum AccountType {

    PASSWORD("密码"),
    WECHAT("微信"),
    WORK_WECHAT("企业微信"),
    SMS("短信"),
    ALIPAY("支付宝");


    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据描述获取账号类型（忽略大小写）
     */
    public static Optional<AccountType> fromDescription(String description) {
        if (description == null || description.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(p -> p.getDescription().equalsIgnoreCase(description))
                .findFirst();
    }

    /**
     * 获取所有账号类型描述
     */
    public static Set<String> getAllDescriptions() {
        return Arrays.stream(values())
                .map(AccountType::getDescription)
                .sorted()
                .collect(Collectors.toSet());
    }

    /**
     * 获取所有账号类型名称（枚举名）
     */
    public static Set<String> getAllNames() {
        return Arrays.stream(values())
                .map(Enum::name)
                .sorted()
                .collect(Collectors.toSet());
    }

    /**
     * 转换为字符串数组（账号类型描述）
     */
    public static String[] getDescriptionArray() {
        return getAllDescriptions().toArray(new String[0]);
    }

    /**
     * 转换为字符串数组（账号类型名称）
     */
    public static String[] getNameArray() {
        return getAllNames().toArray(new String[0]);
    }
}
