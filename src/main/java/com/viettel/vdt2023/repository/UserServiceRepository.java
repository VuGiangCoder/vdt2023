package com.viettel.vdt2023.repository;

import com.viettel.vdt2023.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserServiceRepository extends JpaRepository<UserServiceEntity, Long> {
    UserServiceEntity findByUserAndService(UserEntity user, ServiceEntity service);
}
