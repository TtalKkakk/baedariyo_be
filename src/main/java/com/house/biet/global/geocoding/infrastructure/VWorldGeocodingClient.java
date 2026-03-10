package com.house.biet.global.geocoding.infrastructure;

import com.house.biet.global.geocoding.config.VWorldProperties;
import com.house.biet.global.geocoding.dto.GeoPoint;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class VWorldGeocodingClient {

    private final WebClient vworldWebClient;
    private final VWorldProperties properties;

    public VWorldGeocodingClient(
            @Qualifier("vworldWebClient") WebClient vworldWebClient,
            VWorldProperties properties
    ) {
        this.vworldWebClient = vworldWebClient;
        this.properties = properties;
    }

    public GeoPoint geocode(String address) {

        VWorldResponse response =
                vworldWebClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/req/address")
                                .queryParam("service", "address")
                                .queryParam("request", "getcoord")
                                .queryParam("crs", "epsg:4326")
                                .queryParam("address", address)
                                .queryParam("format", "json")
                                .queryParam("type", "road")
                                .queryParam("key", properties.getRestApiKey())
                                .build())
                        .retrieve()
                        .onStatus(
                                HttpStatusCode::is5xxServerError,
                                clientResponse -> Mono.error(new CustomException(ErrorCode.GEOCODING_API_ERROR))
                        )
                        .bodyToMono(VWorldResponse.class)
                        .timeout(Duration.ofSeconds(2))
                        .retry(2)
                        .block();

        if (response == null ||
                response.response() == null ||
                !"OK".equals(response.response().status()) ||
                response.response().result() == null ||
                response.response().result().point() == null) {
            throw new CustomException(ErrorCode.ADDRESS_GEOCODING_FAILED);
        }

        VWorldResponse.Point point =
                response.response().result().point();

        return new GeoPoint(
                Double.parseDouble(point.y()),
                Double.parseDouble(point.x())
        );
    }
}
