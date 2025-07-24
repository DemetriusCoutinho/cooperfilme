package com.nava.service.cooperfilme.script.mappers;

import com.nava.service.cooperfilme.script.entities.Script;
import com.nava.service.cooperfilme.script.requests.ScriptRequest;

public class ScriptMapper {
    protected ScriptMapper() throws IllegalAccessException {
        throw new IllegalAccessException("Classe não pode ser instanciada");
    }

    public static Script fromModel(ScriptRequest request) {
        return new Script(
                request.script(),
                request.name(),
                request.email(),
                request.phone()
        );
    }
}
