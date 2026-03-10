package com.house.biet.global.route.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kakao.mobility")
public class KakaoMobilityProperties {

    private String baseUrl;
    private String apiKey;
}