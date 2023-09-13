package com.viettel.vdt2023.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class SignupRequestEntity {

    private String username;

    private String password;

    private Long gitlabId;

    private List<String> roles;

}
