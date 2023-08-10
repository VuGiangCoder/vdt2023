package com.viettel.vdt2023.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateServiceRequestEntity {
    private String name;
    private String description;
    private String parentGroupName;
}
