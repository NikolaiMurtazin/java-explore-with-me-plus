package ru.practicum.model;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.stat.EndpointHitDTO;
import ru.practicum.stat.ViewStatsDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EndpointMapper {

    EndpointHit toEndpointHit(EndpointHitDTO dto);

    EndpointHitDTO toEndpointHitDTO(EndpointHit endpointHit);

    ViewStatsDTO toViewStatsDTO(EndpointHit model);
}
