package com.viettel.vdt2023.repository;

import com.viettel.vdt2023.entity.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SystemRepository extends JpaRepository<SystemEntity, Long> {
    SystemEntity findByName(String systemName);

    List<SystemEntity> findAll();

    @Override
    Optional<SystemEntity> findById(Long id);
}
