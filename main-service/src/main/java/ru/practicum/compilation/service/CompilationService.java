package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.PublicCompilationParams;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    void delete(long compId);

    CompilationDto update(long compId, NewCompilationDto newCompilationDto);

    List<CompilationDto> getAll(PublicCompilationParams params);

    CompilationDto getById(long compId);
}
