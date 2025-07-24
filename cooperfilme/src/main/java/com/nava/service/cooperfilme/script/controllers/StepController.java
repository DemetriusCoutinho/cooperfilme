package com.nava.service.cooperfilme.script.controllers;

import com.nava.service.cooperfilme.common.exceptions.UnauthorizedException;
import com.nava.service.cooperfilme.script.requests.ScriptLinkRequest;
import com.nava.service.cooperfilme.script.requests.StepRequest;
import com.nava.service.cooperfilme.script.services.RefusedScripService;
import com.nava.service.cooperfilme.script.services.StatusScriptService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/v1/steps")
public class StepController {
    private final StatusScriptService statusScriptService;
    private final RefusedScripService refusedScripService;

    public StepController(
            StatusScriptService statusScriptService,
            RefusedScripService refusedScripService
    ) {
        this.statusScriptService = statusScriptService;
        this.refusedScripService = refusedScripService;
    }

    @PostMapping("/link/scripts")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void linkScriptUser(@RequestBody @Valid ScriptLinkRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        statusScriptService.linkScriptUser(request, authentication);
    }

    @PostMapping("/next/{uuid}")
    public void nextStep(
            @PathVariable(value = "uuid") UUID uuidStatusScript,
            @RequestBody(required = false) StepRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        statusScriptService.nextStep(uuidStatusScript, authentication, request);
    }

    @PostMapping("/refused/{uuid}")
    public void refusedScript(
            @PathVariable(value = "uuid") UUID uuidStatusScript,
            @RequestBody StepRequest request
    ) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        boolean hasRole = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().contains("ANALISTA")
                        || auth.getAuthority().contains("APROVADOR"));
        if (!hasRole)
            throw new UnauthorizedException("Você não tem permissão para recusar o roteiro");
        refusedScripService.refused(
                uuidStatusScript,
                request.justification(),
                (UserDetails) authentication.getPrincipal()
        );
    }
}
