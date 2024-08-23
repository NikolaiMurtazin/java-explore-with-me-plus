package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.EndpointMapper;
import ru.practicum.repository.StatRepository;
import ru.practicum.stat.EndpointHitDTO;
import ru.practicum.stat.StatsParams;
import ru.practicum.stat.ViewStatsDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneralStatService implements StatService {

    private final StatRepository statRepository;

    @Override
    @Transactional
    public EndpointHitDTO save(EndpointHitDTO dto) {
        EndpointHit endpointHit = statRepository.save(EndpointMapper.INSTANCE.toEndpointHit(dto));

        return EndpointMapper.INSTANCE.toEndpointHitDTO(endpointHit);
    }

    @Override
    public List<ViewStatsDTO> getStats(StatsParams params) {
        return List.of();
    }
}
