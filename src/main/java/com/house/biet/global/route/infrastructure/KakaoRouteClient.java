package com.house.biet.global.route.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoRouteClient {

    private final WebClient webClient;

    public int requestRouteTime(
            double startLat,
            double startLng,
            double endLat,
            double endLng
    ) {
        KakaoDirectionsResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/directions")
                        .queryParam("origin", startLng + "," + startLat)
                        .queryParam("destination", endLng + "," + endLat)
                        .build())
                .retrieve()
                .bodyToMono(KakaoDirectionsResponse.class)
                .block();

        return response.routes()
                .get(0)
                .summary()
                .duration() / 60;
    }
}
