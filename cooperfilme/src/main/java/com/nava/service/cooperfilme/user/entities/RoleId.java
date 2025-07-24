package com.nava.service.cooperfilme.user.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class RoleId implements Serializable {
    private String name;
    @Column(name = "id_user")
    private Long idUser;

    public RoleId(
            String name,
            Long idUser
    ) {
        this.name = name;
        this.idUser = idUser;
    }

    public RoleId() {
    }

    public String getName() {
        return name;
    }
}
