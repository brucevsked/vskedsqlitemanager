package com.vsked.jpa.po;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Table(name = "role")
@Entity
public class RolePO implements Serializable {

    @Transient
    private static final long serialVersionUID = 85694495677567523L;

    @Id
    private Long id;
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "rolePermission",joinColumns = {@JoinColumn(name = "roleId")},inverseJoinColumns = {@JoinColumn(name = "permissionId")})
    private List<PermissionPO> permissions;

    public RolePO() {
    }

    public RolePO(Long id, String name, List<PermissionPO> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PermissionPO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionPO> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name=" + name +
                ", permissions=" + permissions +
                "}";
    }
}
