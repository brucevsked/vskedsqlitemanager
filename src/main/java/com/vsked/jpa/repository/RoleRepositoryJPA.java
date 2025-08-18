package com.vsked.jpa.repository;

import com.vsked.jpa.po.RolePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepositoryJPA extends JpaRepository<RolePo, Long>, JpaSpecificationExecutor<RolePo> {
}
