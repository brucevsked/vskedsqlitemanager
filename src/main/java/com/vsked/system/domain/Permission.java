package com.vsked.system.domain;

public class Permission {

    private PermissionId id;
    private PermissionName name;
    private PermissionContent content;

    public Permission(PermissionId id, PermissionName name, PermissionContent content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }
    public PermissionId getId() {
        return id;
    }
    public PermissionName getName() {
        return name;
    }
    public PermissionContent getContent() {
        return content;
    }
}
