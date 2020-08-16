package com.interview.template.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationConfiguration {

    private Set<String> blacklist;

    public Set<String> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Set<String> blacklist) {
        this.blacklist = blacklist;
    }
}
