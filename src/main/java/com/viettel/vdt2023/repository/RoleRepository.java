package com.viettel.vdt2023.repository;


import com.viettel.vdt2023.entity.ERole;
import com.viettel.vdt2023.entity.RoleEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository {
    Optional<RoleEntity> findByName(ERole name);
}
