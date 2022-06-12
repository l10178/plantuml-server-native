package com.nxest.plantuml.controller;

import com.nxest.plantuml.service.UmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@Controller
public class CheckSyntaxController {

    private final UmlService umlService;

    @Autowired
    public CheckSyntaxController(UmlService umlService) {
        this.umlService = umlService;
    }

    @GetMapping("/check/{encoded}")
    public Mono<ResponseEntity<byte[]>> checkSyntax(@PathVariable(name = "encoded") String encoded) {
        return Mono.just(umlService.sendCheck(encoded));
    }

}
