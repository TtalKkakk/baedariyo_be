package com.house.biet.global.geocoding.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.vworld")
public class VWorldProperties {

    private String baseUrl;
    private String restApiKey;

}