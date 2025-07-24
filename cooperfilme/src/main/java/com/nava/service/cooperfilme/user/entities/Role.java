package com.nava.service.cooperfilme.user.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ROLE")
public class Role {
    @EmbeddedId
    private RoleId id;

    public Role() {
    }

    public Role(RoleId id) {
        this.id = id;
    }

    public String getName() {
        return id.getName();
    }
}
