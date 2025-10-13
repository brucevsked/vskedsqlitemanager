package com.vsked.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuRepositoryJPA extends JpaRepository<MenuPo, Long>, JpaSpecificationExecutor<MenuPo> {
}
