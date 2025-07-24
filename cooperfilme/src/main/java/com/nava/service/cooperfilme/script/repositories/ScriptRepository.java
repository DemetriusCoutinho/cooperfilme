package com.nava.service.cooperfilme.script.repositories;

import com.nava.service.cooperfilme.script.entities.Script;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ScriptRepository extends JpaRepository<Script, Long>, JpaSpecificationExecutor<Script> {

    List<Script> findBynameClientAndEmailAndPhone(
            String nameClient,
            String email,
            String phone
    );

}
