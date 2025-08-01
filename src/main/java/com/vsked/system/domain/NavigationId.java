package com.vsked.system.domain;

public class NavigationId {

    private Long id;

    public NavigationId(Long id) {
        if(id==null){
            throw new IllegalArgumentException("navigation id not be null！");
        }
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String toString() {
        return "" +id;
    }
}
