package com.nxest.plantuml.controller;


import com.nxest.plantuml.service.UmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Controller
public class PngController {

    private final UmlService umlService;

    @Autowired
    public PngController(UmlService umlService) {
        this.umlService = umlService;
    }

    @GetMapping(value = {"/png/{encoded}", "/png/{idx}/{encoded}", "/img/{encoded}", "/img/{idx}/{encoded}"})
    public Mono<ResponseEntity<byte[]>> png(@PathVariable(name = "idx", required = false) Integer index,
                                            @PathVariable(name = "encoded", required = false) String encoded) {
        return Mono.just(umlService.renderByEncodedUrl(encoded, "png", index));
    }

    @PostMapping(value = {"/png/{encoded}", "/png/{idx}/{encoded}", "/img/{encoded}", "/img/{idx}/{encoded}"})
    public Mono<ResponseEntity<byte[]>> png(@RequestBody Mono<String> uml, @PathVariable(name = "idx", required = false) Integer index,
                                            @PathVariable(name = "encoded", required = false) String encoded) {
        return uml.map(s -> umlService.renderByUml(s, "png", index));
    }

}
