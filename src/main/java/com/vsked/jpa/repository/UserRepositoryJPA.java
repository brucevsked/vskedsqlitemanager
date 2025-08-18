package com.vsked.jpa.repository;

import com.vsked.jpa.po.UserPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface UserRepositoryJPA extends JpaRepository<UserPo, Long>, JpaSpecificationExecutor<UserPo> {

    Optional<UserPo> findByName(String name);
    Optional<UserPo> findByCertificateId(Long certificateId);
}
