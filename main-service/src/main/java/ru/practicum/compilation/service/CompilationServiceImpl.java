package ru.practicum.compilation.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.client.StatClient;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.PublicCompilationParams;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.QCompilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.stat.StatsParams;
import ru.practicum.stat.ViewStatsDTO;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventService eventService;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final StatClient statClient;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final EventMapper eventMapper;


    private final CompilationMapper compilationMapper;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Collection<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventService.getByIds(newCompilationDto.getEvents());
        }
        Compilation compilation = compilationMapper.toEntity(newCompilationDto, events);
        //TODO достать views User requests и положить в маппер
        Compilation saved = compilationRepository.save(compilation);
        List<EventShortDto> list = getEventShortDtos(saved);
        return compilationMapper.toCompilationDto(saved, list);
    }

    private List<EventShortDto> getEventShortDtos(Compilation saved) {
        Collection<Event> compEvents = saved.getEvents();


        List<EventShortDto> list = compEvents.stream().map(ev -> {
            Long request = requestRepository.countConfirmedRequest(ev.getId());
            User user = ev.getInitiator();
            UserShortDto userDto = userMapper.toUserShortDto(user);

            List<String> listEndpoint = List.of("/events/" + ev.getId());
            StatsParams statsParams = StatsParams.builder()
                    .uris(listEndpoint)
                    .unique(false)
                    .start(LocalDateTime.MIN)
                    .end(LocalDateTime.now())
                    .end(LocalDateTime.now()).build();
            List<ViewStatsDTO> stats = statClient.getStats(statsParams);
            long views;
            if (stats.isEmpty()) {
                views = 0L;
            } else {
                views = stats.getFirst().getHits();
            }
            return eventMapper.toShortDto(ev, views, request);
        }).toList();
        return list;
    }

    @Override
    @Transactional
    public void delete(long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id= " + compId + " was not found"));

        compilationRepository.deleteById(compId);

    }

    @Override
    @Transactional
    public CompilationDto update(long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id= " + compId + " was not found"));
        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(eventService.getByIds(newCompilationDto.getEvents()));
        }
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        compilation.setPinned(newCompilationDto.isPinned());
        //TODO достать views  requests и положить в маппер
        Compilation saved = compilationRepository.save(compilation);
        List<EventShortDto> list = getEventShortDtos(saved);

        return compilationMapper.toCompilationDto(saved, list);
    }

    @Override
    public List<CompilationDto> getAll(PublicCompilationParams params) {
        QCompilation compilation = QCompilation.compilation;
        List<BooleanExpression> conditions = new ArrayList<>();

        if (params.getPinned() != null) {
            conditions.add(compilation.pinned.eq(params.getPinned()));
        }

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .orElse(null);

        PageRequest pageRequest = PageRequest.of(params.getFrom() / params.getSize(), params.getSize());

        assert finalCondition != null;
        Iterable<Compilation> compilationsIterable = compilationRepository.findAll(finalCondition, pageRequest);

        List<Compilation> compilations = StreamSupport.stream(compilationsIterable.spliterator(), false)
                .toList();

        return compilations.stream().map(comp ->
                compilationMapper.toCompilationDto(comp, getEventShortDtos(comp))).toList();
    }

    @Override
    public CompilationDto getById(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id= " + compId + " was not found"));

        return compilationMapper.toCompilationDto(compilation, getEventShortDtos(compilation));
    }
}
