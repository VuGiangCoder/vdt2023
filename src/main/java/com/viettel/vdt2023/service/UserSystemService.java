package com.viettel.vdt2023.service;

import com.viettel.vdt2023.entity.SystemEntity;
import com.viettel.vdt2023.entity.UserEntity;
import com.viettel.vdt2023.entity.UserSystemEntity;
import com.viettel.vdt2023.repository.UserSystemRepository;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

@Service
@RequiredArgsConstructor
@Component
public class UserSystemService {
    private final UserSystemRepository userSystemRepository;
    public void saveUserSystem(UserSystemEntity userSystemEntity){
        userSystemRepository.save(userSystemEntity);
    }
    public UserSystemEntity loadUserSystem(UserEntity user, SystemEntity system){
        return userSystemRepository.findByUserAndSystem(user,system);
    }
}
