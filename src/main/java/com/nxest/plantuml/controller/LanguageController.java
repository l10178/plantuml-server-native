package com.nxest.plantuml.controller;


import com.nxest.plantuml.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class LanguageController {

    private final LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping(value = {"/language"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> printLanguages() {
        return Mono.just(languageService.printLanguages());
    }
}
