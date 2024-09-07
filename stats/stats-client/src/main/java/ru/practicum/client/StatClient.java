package ru.practicum.client;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stat.EndpointHitDTO;
import ru.practicum.stat.StatsParams;
import ru.practicum.stat.ViewStatsDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@Component
public class StatClient {

    private final WebClient webClient;
    private final String startUrl;

    @Autowired
    public StatClient(@Value("${client.url}") String startUrl) {
        this.startUrl = startUrl;
        this.webClient = WebClient.builder()
                .baseUrl(startUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void saveStats(HttpServletRequest request) {
        try {
            String ip = request.getRemoteAddr();
            String uri = request.getRequestURI();
            EndpointHitDTO dto = EndpointHitDTO.builder()
                    .app("ewm-main-service")
                    .ip(ip)
                    .uri(uri)
                    .timestamp(LocalDateTime.now())
                    .build();

            webClient.post()
                    .uri("/hit")
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            log.warn("Failed to save stats: {}", e.getMessage());
        }
    }

    public List<ViewStatsDTO> getStats(StatsParams params) {
        try {
            String uri = UriComponentsBuilder.fromPath("/stats")
                    .queryParam("start", encodeDate(params.getStart()))
                    .queryParam("end", encodeDate(params.getEnd()))
                    .queryParam("uris", params.getUris())
                    .queryParam("unique", params.getUnique())
                    .toUriString();

            return webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<ViewStatsDTO>>() {})
                    .block();
        } catch (Exception e) {
            log.warn("Failed to get stats: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    private String encodeDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }
}

