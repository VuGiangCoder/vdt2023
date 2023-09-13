package com.viettel.vdt2023.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter@Setter
public class JwtResponseEntity {
    private String token;

    private Long id;

    private Long gitlabId;

    private String username;

    private List<String> roles;
}
