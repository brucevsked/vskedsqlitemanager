package com.vsked.system.domain;

public class NavigationName {

    private String name;

    private final Integer minLength=2;

    private final Integer maxLength=64;


    public NavigationName(String name) {
        if(name==null){
            throw new IllegalArgumentException("navigation name not be null！");
        }

        String trimName=name.replace(" ","");
        if("".equals(trimName)){
            throw new IllegalArgumentException("navigation name not be empty string！");
        }

        if(name.length()<minLength){
            throw new IllegalArgumentException("navigation name too short！not less than "+minLength+" letters。");
        }

        if(name.length()>maxLength){
            throw new IllegalArgumentException("navigation name too long！not more than "+maxLength+" letters。");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
