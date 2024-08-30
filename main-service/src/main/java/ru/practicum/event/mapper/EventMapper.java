package ru.practicum.event.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EventMapper {

    EventFullDto toEventFullDto(final Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "views", source = "views")
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    @Mapping(target = "compilations", ignore = true)
    @Mapping(target = "category", source = "category")
    Event toEvent(final NewEventDto newEventDto, final LocalDateTime createdOn, final User initiator,
                  EventState state, final Category category, final Location location,
                  int confirmedRequests, long views);

    @Mapping(target = "views", source = "views")
    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    @Mapping(target = "id", source = "event.id")
    EventShortDto toShortDto(final Event event, long views, long confirmedRequests);
    //TODO нужны будут кастомные мапперы, т.к. поля отличаются
}
