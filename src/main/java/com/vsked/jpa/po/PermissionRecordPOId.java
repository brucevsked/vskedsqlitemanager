package com.vsked.jpa.po;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import java.io.Serializable;

@Embeddable
public class PermissionRecordPOId implements Serializable {

    @Transient//此字段不需要持久化到数据库
    private static final long serialVersionUID = 1535847169337221935L;

    @Column(name = "permissionId")
    private Long permissionId;
    @Column(name = "recordId")
    private Long recordId;

    public PermissionRecordPOId() {
    }

    public PermissionRecordPOId(Long permissionId, Long recordId) {
        this.permissionId = permissionId;
        this.recordId = recordId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    @Override
    public String toString() {
        return "{" +
                "permissionId=" + permissionId +
                ", recordId=" + recordId +
                "}";
    }
}
