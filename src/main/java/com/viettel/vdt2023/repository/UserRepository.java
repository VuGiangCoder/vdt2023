package com.viettel.vdt2023.repository;

import com.viettel.vdt2023.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    Boolean existsByUsername(String username);

}
