package com.vsked.system.repository;

import com.vsked.system.domain.Page;
import com.vsked.system.domain.Role;
import com.vsked.system.domain.RoleId;
import com.vsked.system.domain.RoleName;
import java.util.List;

public interface RoleRepository {

    List<Role> findAll();
    List<Role> findAllByIds(List<RoleId> roleIds);
    Boolean isExistBy(RoleName roleName);
    Boolean isExistBy(RoleId roleId,RoleName roleName);
    Boolean isExistBy(RoleId roleId);
    Role getBy(RoleId roleId);
    Role getBy(RoleName roleName);
    Role getByIdNotAndNameIs(RoleId roleId,RoleName name);
    void save(Role role);
    Long nextRoleId();
    Page<Role> findAll(Page<Role> page);
}
