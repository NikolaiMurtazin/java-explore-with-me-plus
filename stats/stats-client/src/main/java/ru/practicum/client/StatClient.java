package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.stat.StatsParams;
import ru.practicum.stat.EndpointHitDTO;
import ru.practicum.stat.ViewStatsDTO;

import java.util.List;
import java.util.Map;

@Component
public class StatClient {
    private final RestClient restClient;

    @Autowired
    public StatClient(@Value("${client.url") String startUrl) {
        restClient = RestClient.builder()
                .baseUrl(startUrl)
                .build();
    }

    public ResponseEntity<Object> saveStats(EndpointHitDTO dto) {
        return restClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

    }

    public List<ViewStatsDTO> getStats(StatsParams params) {
        Map<String, Object> pathParams = Map.of(
                "start", params.getStart(),
                "end", params.getEnd(),
                "uris", params.getUris(),
                "unique", params.getUnique()
        );
        return restClient.get()
                .uri("/stats", pathParams)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
