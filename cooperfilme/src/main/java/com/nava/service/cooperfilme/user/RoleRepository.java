package com.nava.service.cooperfilme.user;

import com.nava.service.cooperfilme.user.entities.Role;
import com.nava.service.cooperfilme.user.entities.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {
}
