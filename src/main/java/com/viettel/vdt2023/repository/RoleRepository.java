package com.viettel.vdt2023.repository;


import com.viettel.vdt2023.entity.ERole;
import com.viettel.vdt2023.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    Optional<RoleEntity> findByName(ERole name);
}
