package com.viettel.vdt2023.repository;

import com.viettel.vdt2023.entity.SystemEntity;
import com.viettel.vdt2023.entity.UserEntity;
import com.viettel.vdt2023.entity.UserSystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSystemRepository extends JpaRepository<UserSystemEntity, Long> {
    UserSystemEntity findByUserAndSystem(UserEntity user, SystemEntity system);
}
