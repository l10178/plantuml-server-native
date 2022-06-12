package com.nxest.plantuml.service;


import com.nxest.plantuml.config.PlantumlServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;

import static com.nxest.plantuml.config.Constants.DEFAULT_ERROR_RESPONSE;

@Service
public class ProxyService {
    private static final Logger log = LoggerFactory.getLogger(ProxyService.class);

    private final PlantumlServerConfig config;
    private final UmlService umlService;

    @Autowired
    public ProxyService(PlantumlServerConfig config, UmlService umlService) {
        this.config = config;
        this.umlService = umlService;
    }

    public Mono<ResponseEntity<byte[]>> renderByUrl(String source, String format, Integer index) {
        return initWebClient(source)
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .filter(s -> s != null && !s.isBlank())
                // Get puml string
                .map(uml -> umlService.renderByUml(uml, format, index))
                //on error or on empty, return 400
                .onErrorResume(e -> {
                    log.error("Render error.", e);
                    return Mono.just(DEFAULT_ERROR_RESPONSE);
                })
                .defaultIfEmpty(DEFAULT_ERROR_RESPONSE);
    }

    private WebClient initWebClient(String source) {
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(source)
                // set http timeout,default 30s is too long
                .clientConnector(connector());
        // add Authorization Header
        addAuthorization(builder);
        return builder.build();
    }

    private ReactorClientHttpConnector connector() {
        return new ReactorClientHttpConnector(
                HttpClient.create()
                        .responseTimeout(config.getProxyTimeout())
        );
    }

    private void addAuthorization(WebClient.Builder builder) {
        Optional.ofNullable(config.getProxyAuthorization())
                .ifPresent(s -> builder.defaultHeader("Authorization", s));
    }


}
