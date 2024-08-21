package ru.practicum.model;

import ru.practicum.stat.EndpointHitDTO;

public class EndpointMapper {

    public EndpointHit mapToModel(EndpointHitDTO dto) {
        return EndpointHit.builder()
                .ip(dto.getIp())
                .app(dto.getApp())
                .uri(dto.getUri())
                .build();
    }

    public EndpointHitDTO mapToDto(EndpointHit model) {
        return EndpointHitDTO.builder()
                //надо не надо id по идее нам не нужно dto на возврат
                .ip(model.getIp())
                .app(model.getApp())
                .uri(model.getUri())
                .build();
    }
}
