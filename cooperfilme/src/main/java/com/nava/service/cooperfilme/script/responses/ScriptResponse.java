package com.nava.service.cooperfilme.script.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ScriptResponse(
        String id,
        String name,
        String script,
        String email,
        String status,
        LocalDateTime createdAt
) {
}
