package com.nxest.plantuml.service;

import net.sourceforge.plantuml.version.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class WebModelService {

    private final TranscoderService transcoderService;
    private final UmlService umlService;

    @Autowired
    public WebModelService(TranscoderService transcoderService, UmlService umlService) {
        this.transcoderService = transcoderService;
        this.umlService = umlService;
    }

    public Mono<Rendering> renderByEncoded(String encoded) {
        ModelMap modelMap = new ModelMap();

        // add editor form text
        String text = transcoderService.decode(encoded);
        modelMap.addAttribute("text", text);

        modelMap.addAttribute("imageUrl", "/png/" + encoded);
        modelMap.addAttribute("svgUrl", "/svg/" + encoded);
        modelMap.addAttribute("txtUrl", "/txt/" + encoded);
        modelMap.addAttribute("mapUrl", "/map/" + encoded);

        // map for diagram source if necessary
        String map = umlService.extractMap(text, 0);
        Optional.ofNullable(map)
                .ifPresent(s -> {
                    modelMap.addAttribute("hasMap", true);
                    modelMap.addAttribute("map", s);
                });

        modelMap.addAttribute("version", Version.versionString());
        return Mono.just(Rendering.view("uml/index.html").model(modelMap).build());
    }

    public Mono<Rendering> postBody(ServerWebExchange exchange) {
        return exchange.getFormData()
                .flatMap(map -> Mono.justOrEmpty(map.getFirst("text")))
                .map(transcoderService::encode)
                .map(encoded -> Rendering.redirectTo("/uml/" + encoded).build());
    }

    public Mono<Rendering> postUrl(ServerWebExchange exchange) {
        return exchange.getFormData()
                .flatMap(map -> Mono.justOrEmpty(map.getFirst("url")))
                .map(transcoderService::getEncodedDiagram)
                .map(encoded -> Rendering.redirectTo("/uml/" + encoded).build());
    }
}
