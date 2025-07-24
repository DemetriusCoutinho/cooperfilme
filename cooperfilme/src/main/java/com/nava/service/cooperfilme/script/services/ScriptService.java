package com.nava.service.cooperfilme.script.services;

import com.nava.service.cooperfilme.common.exceptions.NotFoundException;
import com.nava.service.cooperfilme.script.entities.Script;
import com.nava.service.cooperfilme.script.mappers.ScriptMapper;
import com.nava.service.cooperfilme.script.repositories.ScriptRepository;
import com.nava.service.cooperfilme.script.requests.ScriptRequest;
import com.nava.service.cooperfilme.script.requests.StatusScriptRequest;
import com.nava.service.cooperfilme.script.responses.StatusScriptResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScriptService {
    private final ScriptRepository repository;
    private final StatusScriptService statusScriptService;

    public ScriptService(
            ScriptRepository repository,
            StatusScriptService statusScriptService
    ) {
        this.repository = repository;
        this.statusScriptService = statusScriptService;
    }

    public void create(ScriptRequest request) {
        Script script = ScriptMapper.fromModel(request);
        Script save = repository.save(script);
        statusScriptService.createStatusInitial(save);
    }

    public List<StatusScriptResponse> findStatus(StatusScriptRequest request) {
        List<Script> scripts = repository.findBynameClientAndEmailAndPhone(
                request.name(),
                request.email(),
                request.phone()
        );
        if (scripts.isEmpty())
            throw new NotFoundException("Não foram encontrados roteiros para esse Cliente");
        return scripts
                .stream()
                .map(script ->
                        new StatusScriptResponse(
                                script.getScriptText(),
                                statusScriptService.findStatus(script.getId()
                                )
                        ))
                .toList();
    }
}
