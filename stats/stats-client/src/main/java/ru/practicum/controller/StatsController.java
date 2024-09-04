package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
public class StatsController {

//    private final StatClient statClient;

//    @PostMapping("/hit")
//    public void saveStats(@Valid @RequestBody EndpointHitDTO hitDto) {
//        statClient.saveStats(hitDto);
//    }
//
//    @GetMapping("/stats")
//    public List<ViewStatsDTO> getStats(@Valid @RequestParam("start") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
//                                       @Valid @RequestParam("end") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
//                                       @RequestParam("uris") List<String> uris,
//                                       @RequestParam(value = "unique", defaultValue = "false") Boolean unique) {
//        StatsParams statsParams = StatsParams.builder()
//                .start(start)
//                .end(end)
//                .unique(unique)
//                .uris(uris)
//                .build();
//
//        return statClient.getStats(statsParams);
//    }
}
