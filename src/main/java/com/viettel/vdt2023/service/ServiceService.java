package com.viettel.vdt2023.service;

import com.viettel.vdt2023.entity.ServiceEntity;
import com.viettel.vdt2023.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

@Service
@RequiredArgsConstructor
@Component
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public ServiceEntity loadServiceByName(String serviceName){
        return serviceRepository.findByName(serviceName);
    }
    public void saveService(ServiceEntity serviceEntity){
        serviceRepository.save(serviceEntity);
    }
}
