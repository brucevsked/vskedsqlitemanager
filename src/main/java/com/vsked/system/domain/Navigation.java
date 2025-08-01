package com.vsked.system.domain;

public class Navigation {

    private NavigationId id;
    private NavigationName name;
    private NavigationLevel level;
    private NavigationAddress address;
    private Navigation parent;

    public Navigation(Navigation parent,NavigationId id, NavigationName name, NavigationAddress address) {
        this.parent=parent;
        this.id = id;
        this.name = name;
        this.level = getLevel();
        this.address = address;
    }

    public void checkIsExist(Navigation parentNavigation,NavigationName navigationName,Boolean isExist){
        if(isExist){
            if(parentNavigation==null){
                throw new IllegalArgumentException("has exist same name ["+navigationName+"]，please check！");
            }
            throw new IllegalArgumentException("["+parentNavigation.getName()+"] has exist same name ["+navigationName+"]，please check！");
        }
    }

    public static void checkNotExist(NavigationId navigationId,Boolean isExist){
        if(!isExist){
            throw new IllegalArgumentException("navigation id not exist ["+navigationId+"]！");
        }
    }


    public Navigation getParent() {
        return parent;
    }

    public NavigationId getId() {
        return id;
    }

    public NavigationName getName() {
        return name;
    }

    public NavigationLevel getLevel() {
        if(parent==null){
            level=new NavigationLevel(1);//if current level is top level is 1
        }else{
            level=new NavigationLevel(parent.getLevel().getLevel()+1);//if not top level current level=parent level+1
        }
        return level;
    }

    public NavigationAddress getAddress() {
        return address;
    }

    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"name\":\"" + name +"\""+
                ", \"level\":" + level +
                ", \"address\":\"" + address +"\""+
                ", \"parent\":" + parent +
                "}";
    }
}
