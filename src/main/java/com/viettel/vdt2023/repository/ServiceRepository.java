package com.viettel.vdt2023.repository;

import com.viettel.vdt2023.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    ServiceEntity findByName(String name);

    List<ServiceEntity> findAll();

    Optional<ServiceEntity> findById(Long id);
}
