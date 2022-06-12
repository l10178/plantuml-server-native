package com.nxest.plantuml.controller;

import com.nxest.plantuml.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

/**
 * Render by proxy the source file.
 */
@Controller
public class ProxyController {

    private final ProxyService proxyService;

    @Autowired
    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }


    @GetMapping("/proxy")
    public Mono<ResponseEntity<byte[]>> renderByUrl(@RequestParam(name = "src") String source, @RequestParam(name = "format", required = false) String fmt, @RequestParam(name = "idx", required = false, defaultValue = "0") Integer index) {
        return proxyService.renderByUrl(source, fmt, index);
    }

}
