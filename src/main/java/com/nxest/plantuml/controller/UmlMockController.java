package com.nxest.plantuml.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * Mock a default start uml for easy use.
 */
@Controller
public class UmlMockController {

    @GetMapping("/puml/start.puml")
    public Mono<ResponseEntity<String>> start() {
        String uml = """
                @startuml
                Bob -> Alice : hello
                @enduml
                """;
        return Mono.just(ResponseEntity.ok(uml));
    }

}
