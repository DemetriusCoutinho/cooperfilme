package com.nava.service.cooperfilme.script.services;

import com.nava.service.cooperfilme.common.enums.RolesEnum;
import com.nava.service.cooperfilme.common.enums.StepScript;
import com.nava.service.cooperfilme.common.exceptions.UnauthorizedException;
import com.nava.service.cooperfilme.script.entities.Script;
import com.nava.service.cooperfilme.script.entities.StatusScript;
import com.nava.service.cooperfilme.script.repositories.ScriptRepository;
import com.nava.service.cooperfilme.script.repositories.ScriptSpecification;
import com.nava.service.cooperfilme.script.requests.ScriptFilter;
import com.nava.service.cooperfilme.script.responses.ScriptResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class SearchScriptService {
    private final ScriptRepository repository;
    private final StatusScriptService statusScriptService;

    public SearchScriptService(
            ScriptRepository repository,
            StatusScriptService statusScriptService
    ) {
        this.repository = repository;
        this.statusScriptService = statusScriptService;
    }

    public List<ScriptResponse> findByStep(
            StepScript step,
            Collection<? extends GrantedAuthority> authorities
    ) {
        var hasRole = verifyRolesByStep(step, authorities);
        if (!hasRole)
            throw new UnauthorizedException("Você não tem permissão para visualizar esse recurso!");
        List<StatusScript> statusScripts = statusScriptService.findByStatusIgnoreCase(step);
        return statusScripts.stream().flatMap(it ->
                repository.findById(it.getIdScript())
                        .stream()
                        .map(response ->
                                new ScriptResponse(
                                        it.getUuid().toString(),
                                        response.getNameClient(),
                                        response.getScriptText(),
                                        response.getEmail(),
                                        it.getStatus(),
                                        response.getCreatedAt()
                                )
                        )
        ).toList();
    }

    public List<ScriptResponse> findAllByFilter(ScriptFilter filter) {
        return repository.findAll(ScriptSpecification.filters(filter))
                .stream()
                .map(it -> it.getStatusScripts()
                        .stream()
                        .max(Comparator.comparing(StatusScript::getCreatedAt))
                        .map(status ->
                                new ScriptResponse(
                                        status.getUuid().toString(),
                                        it.getNameClient(),
                                        it.getScriptText(),
                                        it.getEmail(),
                                        status.getStatus(),
                                        it.getCreatedAt()

                                )
                        ).orElse(null)
                ).filter(Objects::nonNull)
                .toList();

    }

    private boolean verifyRolesByStep(
            StepScript step,
            Collection<? extends GrantedAuthority> authorities
    ) {
        if ((step.equals(StepScript.AGUARDANDO_ANALISE) || step.equals(StepScript.EM_ANALISE)) &&
                hasRole(authorities, RolesEnum.ANALISTA))
            return true;
        if ((step.equals(StepScript.AGUARDANDO_REVISAO) || step.equals(StepScript.EM_REVISAO)) &&
                hasRole(authorities, RolesEnum.REVISOR))
            return true;
        if ((step.equals(StepScript.AGUARDANDO_APROVACAO) || step.equals(StepScript.EM_APROVACAO)) &&
                hasRole(authorities, RolesEnum.APROVADOR))
            return true;
        return (step.equals(StepScript.RECUSADO) || step.equals(StepScript.APROVADO));


    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, RolesEnum role) {
        return authorities
                .stream()
                .anyMatch(it -> it.getAuthority()
                        .contains(role.name()
                        )
                );
    }
}
