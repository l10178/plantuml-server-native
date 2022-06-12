package com.nxest.plantuml.config;

import net.sourceforge.plantuml.OptionFlags;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OptionFlagsConfig {

    static {
        OptionFlags.ALLOW_INCLUDE = "true".equalsIgnoreCase(System.getenv("ALLOW_PLANTUML_INCLUDE"));
    }
}
