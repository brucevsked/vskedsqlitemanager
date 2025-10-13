package com.vsked.jpa.po;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;

@Table(name = "permission")
@Entity
public class PermissionPO implements Serializable {

    @Transient
    private static final long serialVersionUID = 4576711227493610705L;

    @Id
    private Long id;
    private String name;
    private Byte type;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "permissionResource",joinColumns = {@JoinColumn(name = "permissionId")},inverseJoinColumns = {@JoinColumn(name = "resourceId")})
    private ResourcePO resource;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "permissionResourceAttribute",joinColumns = {@JoinColumn(name = "permissionId")},inverseJoinColumns = {@JoinColumn(name = "resourceAttributeId")})
    private ResourceAttributePO resourceAttribute;

    @OneToOne(mappedBy = "permission",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private PermissionRecordPO permissionRecord;

    public PermissionPO() {
    }

    public PermissionPO(Long id, String name, Byte type) {
        this.id = id;
        this.name = name;
        this.type = type;
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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public ResourcePO getResource() {
        return resource;
    }

    public void setResource(ResourcePO resource) {
        this.resource = resource;
    }

    public ResourceAttributePO getResourceAttribute() {
        return resourceAttribute;
    }

    public void setResourceAttribute(ResourceAttributePO resourceAttribute) {
        this.resourceAttribute = resourceAttribute;
    }

    public PermissionRecordPO getPermissionRecord() {
        return permissionRecord;
    }

    public void setPermissionRecord(PermissionRecordPO permissionRecord) {
        this.permissionRecord = permissionRecord;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name +
                ", type=" + type +
                ", resource=" + resource +
                ", resourceAttribute=" + resourceAttribute +
                ", permissionRecord=" + permissionRecord +
                "}";
    }
}
