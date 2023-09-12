package com.viettel.vdt2023.repository;

import com.viettel.vdt2023.entity.*;
import com.viettel.vdt2023.gitlab.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserServiceRepository extends JpaRepository<UserServiceEntity, Long> {
    UserServiceEntity findByUserAndService(UserEntity user, ServiceEntity service);

    Optional<UserServiceEntity> findByUser(UserEntity userEntity);
}
