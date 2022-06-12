package com.nxest.plantuml.config;

import org.springframework.http.ResponseEntity;

public interface Constants {

    /**
     * Default response when render error.
     */
    ResponseEntity<byte[]> DEFAULT_ERROR_RESPONSE = ResponseEntity.badRequest().build();

    /**
     * Default encoded uml text.
     * Bob -> Alice : hello
     */
    String DEFAULT_ENCODED_TEXT = "SyfFKj2rKt3CoKnELR1Io4ZDoSa70000";

}
