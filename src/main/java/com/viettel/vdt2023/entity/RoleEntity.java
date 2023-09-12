package com.viettel.vdt2023.entity;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public RoleEntity() {

    }

    public RoleEntity(ERole name) {
        this.name = name;
    }
}
