package com.nava.service.cooperfilme.script.controllers;

import com.nava.service.cooperfilme.script.requests.ScriptRequest;
import com.nava.service.cooperfilme.script.requests.StatusScriptRequest;
import com.nava.service.cooperfilme.script.responses.StatusScriptResponse;
import com.nava.service.cooperfilme.script.services.ScriptService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/scripts")
public class ScriptController {
    private final Logger logger = LoggerFactory.getLogger(ScriptController.class.getSimpleName());
    private final ScriptService service;

    public ScriptController(ScriptService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createScript(@RequestBody @Valid ScriptRequest request) {
        service.create(request);
    }

    @GetMapping("/status")
    public List<StatusScriptResponse> getStatus(@RequestBody @Valid StatusScriptRequest request) {
        return service.findStatus(request);
    }




}
