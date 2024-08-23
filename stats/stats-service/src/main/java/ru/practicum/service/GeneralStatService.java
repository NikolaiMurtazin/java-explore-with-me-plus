package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.repository.StatRepository;
import ru.practicum.stat.EndpointHitDTO;
import ru.practicum.stat.StatsParams;
import ru.practicum.stat.ViewStatsDTO;

import java.util.List;

@RequiredArgsConstructor
public class GeneralStatService implements StatService {

    private final StatRepository statRepository;

    @Override
    public EndpointHitDTO save(EndpointHitDTO dto) {

        return statRepository.save();
    }

    @Override
    public List<ViewStatsDTO> getStats(StatsParams params) {
        return List.of();
    }
}
