package com.viettel.vdt2023.controller;

import com.viettel.vdt2023.entity.SystemEntity;
import com.viettel.vdt2023.service.ServiceService;
import com.viettel.vdt2023.service.SystemService;
import com.viettel.vdt2023.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceManagement {
    @Autowired
    private final UserService userService;

    @Autowired
    private final SystemService systemService;

    @Autowired
    private final ServiceService serviceService;
    @GetMapping("/api/systems")
    public List<SystemEntity> getSystem() {
        return systemService.getAllSystem();
    }

    @GetMapping("/hello")
    public String say(){
        Dotenv dotenv = Dotenv.load();
        return dotenv.get("hello");
    }
}
