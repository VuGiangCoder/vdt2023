package com.viettel.vdt2023.service;

import com.viettel.vdt2023.entity.UserEntity;
import com.viettel.vdt2023.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Service
@Component
public class UserService {

    private final UserRepository userRepository;

    public UserEntity loadUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public void saveUser(UserEntity userEntity){
        userRepository.save(userEntity);
    }

}
