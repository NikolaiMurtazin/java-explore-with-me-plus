package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsParams;
import ru.practicum.client.StatClient;
import ru.practicum.stat.EndpointHitDTO;
import ru.practicum.stat.ViewStatsDTO;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatClient statClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> saveStats(@Valid @RequestBody EndpointHitDTO hitDto) {

        return statClient.saveStats(hitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDTO> getStats(@RequestParam("start") LocalDateTime start,
                                       @RequestParam("end") LocalDateTime end,
                                       @RequestParam("uris") List<String> uris,
                                       @RequestParam(value = "unique", defaultValue = "false") Boolean unique) {
        StatsParams statsParams = StatsParams.builder()
                .start(start)
                .end(end)
                .unique(unique)
                .uris(uris)
                .build();

        return statClient.getStats(statsParams);
    }
}
