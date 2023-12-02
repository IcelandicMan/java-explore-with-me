package ru.practicum.service.compilation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.compilation.CompilationRequestNewDto;
import ru.practicum.mapper.compilation.CompilationRequestUpdateDto;
import ru.practicum.mapper.compilation.CompilationResponseDto;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.UnionService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final UnionService unionService;

    @Override
    public CompilationResponseDto createCompilation(CompilationRequestNewDto compilationRequestUpdateDto) {
        Compilation compilation = CompilationMapper.compilationResponseNewDtoToCompilation(compilationRequestUpdateDto);

        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }
        if (compilationRequestUpdateDto.getEvents() == null || compilationRequestUpdateDto.getEvents().isEmpty()) {
            compilation.setEvents(Collections.emptySet());
        } else {
            compilation.setEvents(eventRepository.findByIdIn(compilationRequestUpdateDto.getEvents()));
        }

        return CompilationMapper.compilationToCompilationResponseDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        unionService.getCompilationOrNotFound(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationResponseDto updateCompilation(Long compId, CompilationRequestUpdateDto compilationUpdateDto) {

        Compilation compilation = unionService.getCompilationOrNotFound(compId);

        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }

        if (compilationUpdateDto.getEvents() == null || compilationUpdateDto.getEvents().isEmpty()) {
            compilation.setEvents(Collections.emptySet());
        } else {
            compilation.setEvents(eventRepository.findByIdIn(compilationUpdateDto.getEvents()));
        }

        if (compilationUpdateDto.getTitle().isPresent()) {
            compilation.setTitle(compilationUpdateDto.getTitle().get());
        }
        return CompilationMapper.compilationToCompilationResponseDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationResponseDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Compilation> compilations;

        if (pinned) {
            compilations = compilationRepository.findByPinned(pinned, pageRequest);
        } else {
            compilations = compilationRepository.findAll(pageRequest).getContent();
        }
        return compilations.stream()
                .map(CompilationMapper::compilationToCompilationResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationResponseDto getCompilationById(Long compId) {
        Compilation compilation = unionService.getCompilationOrNotFound(compId);
        return CompilationMapper.compilationToCompilationResponseDto(compilation);
    }
}
