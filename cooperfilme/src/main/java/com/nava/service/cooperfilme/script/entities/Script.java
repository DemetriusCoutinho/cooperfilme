package com.nava.service.cooperfilme.script.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SCRIPT")
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "scriptText")
    private String scriptText;
    @Column(name = "name")
    private String nameClient;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    @OneToMany
    @JoinColumn(name = "idScript")
    private List<StatusScript> statusScripts = new ArrayList<>();

    public Script(
            String scriptText,
            String nameClient,
            String email,
            String phone
    ) {
        this.id = null;
        this.scriptText = scriptText;
        this.nameClient = nameClient;
        this.email = email;
        this.phone = phone;
        this.createdAt = LocalDateTime.now();
    }

    public Script() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNameClient() {
        return nameClient;
    }

    public String getPhone() {
        return phone;
    }

    public String getScriptText() {
        return scriptText;
    }

    public List<StatusScript> getStatusScripts() {
        return statusScripts;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
