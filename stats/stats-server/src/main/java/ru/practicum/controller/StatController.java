package ru.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exeption.WrongDateException;
import ru.practicum.service.StatService;
import ru.practicum.stat.EndpointHitDTO;
import ru.practicum.stat.StatsParams;
import ru.practicum.stat.ViewStatsDTO;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDTO saveStats(@Valid @RequestBody
                                    EndpointHitDTO hitDto) {
        return statService.save(hitDto);
    }


    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDTO> getStats(@NotNull @RequestParam("start") String start,
                                       @NotNull @RequestParam("end") String end,
                                       @RequestParam(value = "uris", required = false, defaultValue = "") List<String> uris,
                                       @RequestParam(value = "unique", defaultValue = "false") Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String decodedStartDate = URLDecoder.decode(start, StandardCharsets.UTF_8);
        String decodedEndDate = URLDecoder.decode(end, StandardCharsets.UTF_8);
        LocalDateTime startDecode = LocalDateTime.parse(decodedStartDate, formatter);
        LocalDateTime endDecode = LocalDateTime.parse(decodedEndDate, formatter);
        validDate(startDecode, endDecode);

        StatsParams statsParams = StatsParams.builder()
                .start(startDecode)
                .end(endDecode)
                .unique(unique)
                .uris(uris)
                .build();
        return statService.getStats(statsParams);
    }

    private void validDate(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeEnd.isBefore(rangeStart)) {
            throw new WrongDateException("Range end must be after range start");
        }
    }
}
