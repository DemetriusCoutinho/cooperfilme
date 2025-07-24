package com.nava.service.cooperfilme.script.services;

import com.nava.service.cooperfilme.common.enums.StepScript;
import com.nava.service.cooperfilme.common.exceptions.BadRequestException;
import com.nava.service.cooperfilme.common.exceptions.NotFoundException;
import com.nava.service.cooperfilme.common.exceptions.UnauthorizedException;
import com.nava.service.cooperfilme.script.entities.StatusScript;
import com.nava.service.cooperfilme.script.repositories.StatusScriptRepository;
import com.nava.service.cooperfilme.user.UserService;
import com.nava.service.cooperfilme.user.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

@Service
public class RefusedScripService {
    private final StatusScriptRepository repository;
    private final StatusScriptService statusScriptService;
    private final UserService userService;

    public RefusedScripService(
            StatusScriptRepository repository,
            StatusScriptService statusScriptService,
            UserService userService
    ) {
        this.repository = repository;
        this.statusScriptService = statusScriptService;
        this.userService = userService;
    }

    public void refused(
            UUID uuidStatusScript,
            String justification,
            UserDetails user) {
        StatusScript found = repository.findByUuid(uuidStatusScript)
                .orElseThrow(() -> new NotFoundException("Não foi encontrado nenhum roteiro!"));
        boolean assign = isAssign(found.getIdUsuario(), user);
        if (!assign)
            throw new UnauthorizedException("Você não tem autorização pra recusar o Roteiro");
        if (!StringUtils.hasText(justification)) {
            throw new BadRequestException("Justificativa é requerida");
        }
        StatusScript refused = new StatusScript(
                StepScript.RECUSADO.name(),
                found.getIdScript(),
                found.getIdUsuario(),
                found.getUuid()
        );
        refused.applyJustification(justification);
        repository.save(refused);

    }


    private boolean isAssign(Long idUser, UserDetails userDetails) {
        User user = userService.findByName(userDetails.getUsername());
        if (Objects.isNull(idUser))
            return true;
        else return idUser.equals(user.getId());
    }
}
