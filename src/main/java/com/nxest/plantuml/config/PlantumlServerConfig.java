package com.nxest.plantuml.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties("plantuml.server")
public class PlantumlServerConfig {

    private String defaultFileFormat;
    private String proxyAuthorization;
    private Duration proxyTimeout;

    public String getDefaultFileFormat() {
        return defaultFileFormat;
    }

    public void setDefaultFileFormat(String defaultFileFormat) {
        this.defaultFileFormat = defaultFileFormat;
    }

    public String getProxyAuthorization() {
        return proxyAuthorization;
    }

    public void setProxyAuthorization(String proxyAuthorization) {
        this.proxyAuthorization = proxyAuthorization;
    }

    public Duration getProxyTimeout() {
        return proxyTimeout;
    }

    public void setProxyTimeout(Duration proxyTimeout) {
        this.proxyTimeout = proxyTimeout;
    }
}
