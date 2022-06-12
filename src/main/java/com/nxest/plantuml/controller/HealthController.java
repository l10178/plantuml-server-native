package com.nxest.plantuml.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * health check api.
 */
@RestController
public class HealthController {

    @RequestMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("{\"status\":\"UP\"}"));
    }

}
