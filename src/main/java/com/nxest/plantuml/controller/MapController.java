package com.nxest.plantuml.controller;


import com.nxest.plantuml.service.UmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@Controller
public class MapController {

    private final UmlService umlService;

    @Autowired
    public MapController(UmlService umlService) {
        this.umlService = umlService;
    }

    @GetMapping(value = {"/map/{encoded}", "/map/{idx}/{encoded}"})
    public Mono<ResponseEntity<byte[]>> renderMap(@PathVariable(name = "idx", required = false) Integer index,
                                                  @PathVariable(name = "encoded") String encoded) {
        return Mono.just(umlService.renderMap(encoded, index));
    }

}
