package com.vsked.system.domain;

public class Permission {

    private PermissionId id;
    private PermissionName name;
    private PermissionContent content;
    private PermissionType type;
    private Resource resource;
    private ResourceAttribute attribute;
    private ResourceAction action;

    public Permission(PermissionId id, PermissionName name, PermissionContent content, PermissionType type, Resource resource, ResourceAttribute attribute, ResourceAction action) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.type = type;
        this.resource = resource;
        this.attribute = attribute;
        this.action = action;
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

    public PermissionType getType() {
        return type;
    }

    public Resource getResource() {
        return resource;
    }

    public ResourceAttribute getAttribute() {
        return attribute;
    }

    public ResourceAction getAction() {
        return action;
    }
}
