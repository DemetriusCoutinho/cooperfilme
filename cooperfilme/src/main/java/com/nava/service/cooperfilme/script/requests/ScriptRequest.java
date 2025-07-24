package com.nava.service.cooperfilme.script.requests;

import jakarta.validation.constraints.NotBlank;

public record ScriptRequest(
        @NotBlank(message = "Campo script é obrigatório!")
        String script,
        @NotBlank(message = "Campo Nome é obrigatório!")
        String name,
        @NotBlank(message = "Campo email é obrigatório!")
        String email,
        @NotBlank(message = "Campo Telefone é obrigatório!")
        String phone
) {
}
