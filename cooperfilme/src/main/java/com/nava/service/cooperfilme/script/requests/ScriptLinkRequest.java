package com.nava.service.cooperfilme.script.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ScriptLinkRequest(
        @NotNull
        UUID idStatusScript
) {
}
