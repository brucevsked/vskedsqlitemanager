package com.vsked.system.repository;

import com.vsked.system.domain.Permission;
import com.vsked.system.domain.PermissionId;
import com.vsked.system.domain.PermissionName;
import com.vsked.system.domain.PermissionType;
import com.vsked.system.domain.RecordTypeId;
import com.vsked.system.domain.ResourceAttributeName;
import com.vsked.system.domain.ResourceName;
import java.util.List;

public interface PermissionRepository {
    List<Permission> findAll();
    List<Permission> findBy(RecordTypeId recordTypeId);
    List<Permission> findBy(PermissionType permissionType);
    Boolean getIsExistBy(PermissionName permissionName);
    Boolean getIsExistBy(PermissionId permissionId);
    Boolean getIsExistBy(ResourceName resourceName);
    Permission getBy(PermissionId permissionId);
    Permission getBy(PermissionName permissionName);
    Permission getBy(ResourceName resourceName);
    Permission getBy(ResourceName resourceName, ResourceAttributeName attributeName);
    void save(Permission permission);
    Long nextPermissionId();
}
