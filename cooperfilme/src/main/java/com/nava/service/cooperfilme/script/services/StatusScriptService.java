package com.nava.service.cooperfilme.script.services;

import com.nava.service.cooperfilme.common.enums.RolesEnum;
import com.nava.service.cooperfilme.common.exceptions.BadRequestException;
import com.nava.service.cooperfilme.common.exceptions.FlowException;
import com.nava.service.cooperfilme.common.exceptions.NotFoundException;
import com.nava.service.cooperfilme.common.exceptions.UnauthorizedException;
import com.nava.service.cooperfilme.script.entities.Script;
import com.nava.service.cooperfilme.script.entities.StatusScript;
import com.nava.service.cooperfilme.script.repositories.StatusScriptRepository;
import com.nava.service.cooperfilme.common.enums.StepScript;
import com.nava.service.cooperfilme.script.requests.ScriptLinkRequest;
import com.nava.service.cooperfilme.script.requests.StepRequest;
import com.nava.service.cooperfilme.user.UserService;
import com.nava.service.cooperfilme.user.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class StatusScriptService {
    private final StatusScriptRepository repository;
    private final UserService userService;

    public StatusScriptService(
            StatusScriptRepository repository,
            UserService userService
    ) {
        this.repository = repository;
        this.userService = userService;
    }

    public String findStatus(Long idScript) {
        StatusScript statusScript = repository.findByIdScript(idScript)
                .orElseThrow(() -> new NotFoundException("Não foram encontratos roteiros com esses dados!"));
        return statusScript.getStatus();
    }

    public void createStatusInitial(Script script) {
        repository.save(new StatusScript(
                        StepScript.AGUARDANDO_ANALISE.name(),
                        script.getId(),
                        null,
                        UUID.randomUUID()
                )
        );
    }

    public List<StatusScript> findByStatusIgnoreCase(StepScript status) {

        List<StatusScript> scripts = repository.findByStatusIgnoreCaseAndLastUpdateIsNull(status.name());
        if (scripts.isEmpty()) throw new NotFoundException("Não foram encontrados roteiros com esse status!");
        return scripts;
    }

    public void linkScriptUser(
            ScriptLinkRequest request,
            Authentication authentication
    ) {
        StatusScript found = repository.findByUuid(request.idStatusScript())
                .orElseThrow(() -> new NotFoundException("Não foi encontrado nenhum roteiro com as informações!"));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByName(userDetails.getUsername());
        found.changeLastUpdate();
        repository.save(found);
        repository.save(
                new StatusScript(
                        getStatusWithRole(authentication.getAuthorities()).get().name(),
                        found.getIdScript(),
                        user.getId(),
                        found.getUuid()
                )
        );

    }

    private Optional<StepScript> getStatusWithRole(Collection<? extends GrantedAuthority> authorities) {
        if (hasRole(authorities, RolesEnum.ANALISTA))
            return Optional.of(StepScript.EM_ANALISE);
        else if (hasRole(authorities, RolesEnum.REVISOR))
            return Optional.of(StepScript.EM_REVISAO);
        else if (hasRole(authorities, RolesEnum.APROVADOR))
            return Optional.of(StepScript.EM_APROVACAO);
        return Optional.empty();
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, RolesEnum role) {
        return authorities.stream().anyMatch(it -> it.getAuthority().contains(role.name()));
    }

    @Transactional
    public void nextStep(
            UUID uuidStatusScript,
            Authentication authentication,
            StepRequest justification
    ) {
        StatusScript statusScript = repository.findByUuid(uuidStatusScript)
                .orElseThrow(() -> new NotFoundException("Não foi encontrado nenhum roteiro!"));
        boolean assign = isAssign(statusScript.getIdUsuario(), (UserDetails) authentication.getPrincipal());

        if (!assign)
            throw new UnauthorizedException("Você não tem autorização pra mudar o status do Roteiro");

        StepScript nextStep;

        if (!isFinishFlow(statusScript.getStatus())) {
            var count = findCountStatusEmAprovacao(statusScript.getUuid());
            nextStep = getNextStepFlow(StepScript.valueOf(statusScript.getStatus()), count);
        } else
            throw new FlowException("Fluxo do roteiro já finalizado!");

        this.justificationRequired(
                Objects.isNull(justification) ? null : justification.justification(),
                StepScript.valueOf(statusScript.getStatus())
        );
        statusScript.changeLastUpdate();
        if (!Objects.isNull(justification))
            statusScript.applyJustification(justification.justification());
        repository.save(statusScript);
        repository.save(
                new StatusScript(
                        nextStep.name(),
                        statusScript.getIdScript(),
                        statusScript.getIdUsuario(),
                        statusScript.getUuid()
                ));
    }

    private void justificationRequired(String justification, StepScript stepActual) {
        if ((stepActual.equals(StepScript.EM_ANALISE) || (stepActual.equals(StepScript.EM_REVISAO)))
                &&
                !StringUtils.hasText(justification))
            throw new BadRequestException("Você deve informar a justificativa para o Status!");
    }

    private int findCountStatusEmAprovacao(UUID uuid) {
        return repository.findCountByUuidAndStatus(
                uuid,
                StepScript.EM_APROVACAO.name()
        );

    }

    private boolean isAssign(Long idUser, UserDetails userDetails) {
        User user = userService.findByName(userDetails.getUsername());
        if (Objects.isNull(idUser))
            return true;
        else return idUser.equals(user.getId());
    }

    private boolean isFinishFlow(String status) {
        return status.equals(StepScript.APROVADO.name())
                || status.equals(StepScript.RECUSADO.name());
    }

    private StepScript getNextStepFlow(StepScript actualStatus, int count) {
        if (actualStatus.equals(StepScript.AGUARDANDO_ANALISE))
            return StepScript.EM_ANALISE;

        if (actualStatus.equals(StepScript.EM_ANALISE))
            return StepScript.AGUARDANDO_REVISAO;

        if (actualStatus.equals(StepScript.AGUARDANDO_REVISAO))
            return StepScript.EM_REVISAO;

        if (actualStatus.equals(StepScript.EM_REVISAO))
            return StepScript.AGUARDANDO_APROVACAO;

        if (actualStatus.equals(StepScript.AGUARDANDO_APROVACAO))
            return StepScript.EM_APROVACAO;

        if (actualStatus.equals(StepScript.EM_APROVACAO)) {
            if (count >= 3)
                return StepScript.APROVADO;
            else
                return StepScript.EM_APROVACAO;
        } else
            return StepScript.DEFAULT;
    }
}
