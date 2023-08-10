package com.viettel.vdt2023.service;

import com.viettel.vdt2023.entity.*;
import com.viettel.vdt2023.repository.UserServiceRepository;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

@Service
@RequiredArgsConstructor
@Component
public class UserServiceService {
    private final UserServiceRepository userServiceRepository;

    public void saveUserService(UserServiceEntity userServiceEntity){
        userServiceRepository.save(userServiceEntity);
    }
    public UserServiceEntity loadUserService(UserEntity user, ServiceEntity service){
        return userServiceRepository.findByUserAndService(user,service);
    }
}
