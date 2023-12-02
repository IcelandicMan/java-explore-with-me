package ru.practicum.util;

import ru.practicum.model.*;

public interface UnionService {
    User getUserOrNotFound(Long userId);

    Category getCategoryOrNotFound(Long categoryId);

    Event getEventOrNotFound(Long eventId);

    Boolean userIsEventCreator(User user, Event event);

    Request getRequestOrNotFound(Long requestId);

    Compilation getCompilationOrNotFound(Long compId);

}

