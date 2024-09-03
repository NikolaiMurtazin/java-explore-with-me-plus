package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stat.EndpointHitDTO;
import ru.practicum.stat.StatsParams;
import ru.practicum.stat.ViewStatsDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class StatClient {
    private final RestClient restClient;

    @Autowired
    public StatClient(@Value("${client.url}") String startUrl) {
        restClient = RestClient.builder()
                .baseUrl(startUrl)
                .build();
    }

    public void saveStats(EndpointHitDTO dto) {
        try {
            restClient.post()
                    .uri("/hit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public List<ViewStatsDTO> getStats(StatsParams params) {
        try {
            String uri = UriComponentsBuilder.fromPath("/stats")
                    .queryParam("start", encodeDate(params.getStart()))
                    .queryParam("end", encodeDate(params.getEnd()))
                    .queryParam("uris", params.getUris())
                    .queryParam("unique", params.getUnique()).toUriString();
            RestClient.ResponseSpec retrieve = restClient.get()
                    .uri(uri)
                    .retrieve();
            return retrieve.body(new ParameterizedTypeReference<>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private String encodeDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }
}
