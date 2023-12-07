package ru.practicum.util.service;

import ru.practicum.category.model.Category;
import ru.practicum.comment.model.Comment;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

public interface UnionService {
    User getUserOrNotFound(Long userId);

    Category getCategoryOrNotFound(Long categoryId);

    Event getEventOrNotFound(Long eventId);

    Boolean userIsEventCreator(User user, Event event);

    Request getRequestOrNotFound(Long requestId);

    Compilation getCompilationOrNotFound(Long compId);

    Comment getCommentOrNotFound(Long commentId);

    Boolean userIsCommentCreator(User user, Comment comment);

}

