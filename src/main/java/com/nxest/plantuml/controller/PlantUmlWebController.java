package com.nxest.plantuml.controller;

import com.nxest.plantuml.service.WebModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.nxest.plantuml.config.Constants.DEFAULT_ENCODED_TEXT;

/**
 * The default start view api.
 */
@Controller
public class PlantUmlWebController {

    private final WebModelService webModelService;

    @Autowired
    public PlantUmlWebController(WebModelService webModelService) {
        this.webModelService = webModelService;
    }

    @GetMapping("/uml")
    public Rendering start() {
        return Rendering.redirectTo("/uml/" + DEFAULT_ENCODED_TEXT).build();
    }

    @PostMapping(value = "/uml/submitBody", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Rendering> postBody(ServerWebExchange exchange) {
        return webModelService.postBody(exchange);
    }

    @PostMapping(value = "/uml/submitUrl", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Rendering> postUrl(ServerWebExchange exchange) {
        return webModelService.postUrl(exchange);
    }

    @RequestMapping("/uml/{encoded}")
    public Mono<Rendering> encoded(@PathVariable(name = "encoded") String encoded) {
        return webModelService.renderByEncoded(encoded);
    }

}
