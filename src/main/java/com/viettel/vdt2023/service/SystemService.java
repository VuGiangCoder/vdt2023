package com.viettel.vdt2023.service;

import com.viettel.vdt2023.entity.SystemEntity;
import com.viettel.vdt2023.entity.UserEntity;
import com.viettel.vdt2023.repository.SystemRepository;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class SystemService {
    private final SystemRepository systemRepository;

    public SystemEntity loadSystemByName(String systemName) {
        return systemRepository.findByName(systemName);
    }

    public void saveSystem(SystemEntity systemEntity){
        systemRepository.save(systemEntity);
    }

    public List<SystemEntity> getAllSystem(){
       return systemRepository.findAll();
    }
}
