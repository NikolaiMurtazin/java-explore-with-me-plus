package ru.practicum.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.stat.EndpointHitDTO;
import ru.practicum.stat.ViewStatsDTO;

@Mapper
public interface EndpointMapper {
    EndpointMapper INSTANCE = Mappers.getMapper(EndpointMapper.class);

    public EndpointHit toEndpointHit(EndpointHitDTO dto);

    public EndpointHitDTO toEndpointHitDTO(EndpointHit endpointHit);

    public ViewStatsDTO toViewStatsDTO(EndpointHit model);
}
