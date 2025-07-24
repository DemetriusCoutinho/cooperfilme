package com.nava.service.cooperfilme.script.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "STATUS_SCRIPT")
public class StatusScript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private Long idScript;
    private Long idUsuario;
    private UUID uuid;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
    private String justification;

    public StatusScript(
            String status,
            Long idScript,
            Long idUsuario,
            UUID uuid
    ) {
        this.id = null;
        this.status = status;
        this.idScript = idScript;
        this.idUsuario = idUsuario;
        this.uuid = uuid;
        this.createdAt = LocalDateTime.now();
        this.lastUpdate = null;
        this.justification = justification;
    }

    public StatusScript() {
    }

    public Long getIdScript() {
        return idScript;
    }

    public String getStatus() {
        return status;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getJustification() {
        return justification;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void changeLastUpdate() {
        lastUpdate = LocalDateTime.now();
    }

    public void applyJustification(String justification) {
        this.justification = justification;
    }
}
