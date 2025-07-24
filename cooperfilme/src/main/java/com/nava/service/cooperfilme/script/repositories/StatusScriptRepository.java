package com.nava.service.cooperfilme.script.repositories;

import com.nava.service.cooperfilme.script.entities.StatusScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StatusScriptRepository extends JpaRepository<StatusScript, Long> {

    @Query(""" 
            select s from StatusScript s where s.idScript = :idScript and s.createdAt =(
            select MAX(s2.createdAt) From StatusScript s2 where s2.idScript= :idScript)
            """)
    Optional<StatusScript> findByIdScript(Long idScript);

    List<StatusScript> findByStatusIgnoreCaseAndLastUpdateIsNull(String status);

    @Query("""
            select s from StatusScript s where s.uuid = :uuid and s.createdAt =(
                select MAX(s2.createdAt) From StatusScript s2 where s2.uuid= :uuid)
            """)
    Optional<StatusScript> findByUuid(UUID uuid);

    @Query("""
            Select count(*) from StatusScript s where s.uuid = :uuid and s.status = :status
            """)
    int findCountByUuidAndStatus(UUID uuid, String status);

}
