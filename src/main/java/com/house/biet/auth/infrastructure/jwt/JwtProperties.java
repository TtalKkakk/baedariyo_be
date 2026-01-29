package com.house.biet.auth.infrastructure.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {
    private String secretKey;
    private long accessTokenValiditySeconds;
    private long refreshTokenValiditySeconds;
}