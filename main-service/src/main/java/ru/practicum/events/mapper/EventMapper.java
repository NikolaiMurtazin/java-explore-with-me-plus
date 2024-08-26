package ru.practicum.events.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.Event;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    Event toEvent(final NewEventDto newEventDto);

    EventFullDto toFullDto(final Event category);

    EventShortDto toShortDto(final Event category);
    //TODO нужны будут кастомные мапперы, т.к. поля отличаются
}
