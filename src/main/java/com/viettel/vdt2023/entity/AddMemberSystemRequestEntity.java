package com.viettel.vdt2023.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AddMemberSystemRequestEntity {
    private List<String> username;
    private String groupname;
}
