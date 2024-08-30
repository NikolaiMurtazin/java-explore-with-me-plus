package ru.practicum.event.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.location.Location;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "compilations", ignore = true)
    @Mapping(target = "createdOn", source = "now")
    @Mapping(target = "initiator", source = "user")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "location", source = "location")
//TODO
    Event toEntity(final NewEventDto newEventDto, LocalDateTime now, User user, EventState state, Category category, Location location);

    @Mapping(target = "views", source = "views")
    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    EventFullDto toFullDto(final Event event, long views, long confirmedRequests);

    @Mapping(target = "views", source = "views")
    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    @Mapping(target = "id", source = "event.id")
    EventShortDto toShortDto(final Event event, long views, long confirmedRequests);
    //TODO нужны будут кастомные мапперы, т.к. поля отличаются
}
