package com.vsked.jpa.repository;

import com.vsked.jpa.po.ResourcePO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepositoryJPA extends JpaRepository<ResourcePO,Long> {
}
