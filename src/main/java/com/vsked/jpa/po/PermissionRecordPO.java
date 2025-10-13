package com.vsked.jpa.po;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name = "permissionRecord")
public class PermissionRecordPO implements Serializable {

    @Transient
    private static final long serialVersionUID = 8491767417370913513L;

    @EmbeddedId
    private PermissionRecordPOId id;
    private Long typeId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "permissionId",insertable = false,updatable = false)
    private PermissionPO permission;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recordId",insertable = false,updatable = false)
    private RecordPO record;

    public PermissionRecordPO() {
    }

    public PermissionRecordPO(Long typeId, PermissionPO permission, RecordPO record) {
        this.id=new PermissionRecordPOId(permission.getId(),record.getId());
        this.typeId = typeId;
        this.permission = permission;
        this.record = record;
    }

    public PermissionRecordPOId getId() {
        return id;
    }

    public void setId(PermissionRecordPOId id) {
        this.id = id;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public PermissionPO getPermission() {
        return permission;
    }

    public void setPermission(PermissionPO permission) {
        this.permission = permission;
    }

    public RecordPO getRecord() {
        return record;
    }

    public void setRecord(RecordPO record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", typeId=" + typeId +
                ", record=" + record +
                "}";
    }
}
