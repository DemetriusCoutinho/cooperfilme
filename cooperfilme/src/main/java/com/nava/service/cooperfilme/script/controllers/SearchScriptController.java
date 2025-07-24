package com.nava.service.cooperfilme.script.controllers;

import com.nava.service.cooperfilme.common.enums.StepScript;
import com.nava.service.cooperfilme.script.requests.ScriptFilter;
import com.nava.service.cooperfilme.script.responses.ScriptResponse;
import com.nava.service.cooperfilme.script.services.SearchScriptService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/search/scripts")
public class SearchScriptController {
    private final SearchScriptService service;

    public SearchScriptController(SearchScriptService service) {
        this.service = service;
    }

    @GetMapping("/step/{step}")
    public List<ScriptResponse> findByStep(@PathVariable StepScript step) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return service.findByStep(step, authentication.getAuthorities());
    }

    @GetMapping("/step/filter")
    public List<ScriptResponse> findByStep(ScriptFilter filter) {
        return service.findAllByFilter(filter);
    }

}
