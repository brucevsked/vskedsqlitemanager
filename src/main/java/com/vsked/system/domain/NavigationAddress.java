package com.vsked.system.domain;

/**
 * 导航地址
 */
public class NavigationAddress {

    private String address;

    /**
     * 最小长度
     */
    private final Integer minLength=1;

    /**
     * 最大长度
     */
    private final Integer maxLength=256;


    public NavigationAddress(String address) {
        if(address==null){
            throw new IllegalArgumentException("导航地址不能为空！");
        }

        String trimName=address.replace(" ","");
        if("".equals(trimName)){
            throw new IllegalArgumentException("导航地址不能为空字符串！");
        }

        if(address.length()<minLength){
            throw new IllegalArgumentException("导航地址长度过短！不能少于"+minLength+"个字符。");
        }

        if(address.length()>maxLength){
            throw new IllegalArgumentException("导航地址长度过长！不能多于"+maxLength+"个字符。");
        }
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String toString() {
        return address;
    }
}
