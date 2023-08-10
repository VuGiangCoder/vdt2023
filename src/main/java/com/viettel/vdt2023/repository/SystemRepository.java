package com.viettel.vdt2023.repository;

import com.viettel.vdt2023.entity.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SystemRepository extends JpaRepository<SystemEntity, Long> {
    SystemEntity findByName(String systemName);
}
