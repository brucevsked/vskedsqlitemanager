package com.vsked.system.domain;

import java.util.Set;

public class Role {
    private RoleId id;
    private RoleName name;
    private Set<Permission> permissions;

    public Role(RoleId id, RoleName name, Set<Permission> permissions){
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public RoleId getId() {
        return id;
    }

    public RoleName getName() {
        return name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
}
