package com.vsked.system.domain;

public class NavigationLevel {

    private Integer level;

    public NavigationLevel(Integer level) {
        if(level==null){
            throw new IllegalArgumentException("navigation level not be null！");
        }
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public String toString() {
        return ""+ level;
    }
}
