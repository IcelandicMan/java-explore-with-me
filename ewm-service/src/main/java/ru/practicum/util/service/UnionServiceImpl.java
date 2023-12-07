package ru.practicum.util.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.exception.CategoryNotFoundException;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.comment.exeption.CommentNotFoundException;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.compilation.exception.CompilationNotFoundException;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.request.exception.RequestNotOwnerException;
import ru.practicum.request.model.Request;
import ru.practicum.request.service.repository.RequestRepository;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.exception.UserNotOwnerException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UnionServiceImpl implements UnionService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CompilationRepository compilationRepository;
    private final CommentRepository commentRepository;

    @Override
    public User getUserOrNotFound(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(String.format("Пользователь c id %s не найден", userId));
        } else {
            return user.get();
        }
    }

    @Override
    public Category getCategoryOrNotFound(Long categoryId) {

        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isEmpty()) {
            throw new CategoryNotFoundException(String.format("Категория c id %s не найден", categoryId));
        } else {
            return category.get();
        }
    }

    @Override
    public Event getEventOrNotFound(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new EventNotFoundException(String.format("Событие c id %s не найдено", eventId));
        } else {
            return event.get();
        }
    }

    @Override
    public Boolean userIsEventCreator(User user, Event event) {
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new UserNotOwnerException(String.format("Пользователь c id %s не является создателем события с id %s",
                    event.getId(), user.getId()));
        }
        return true;
    }

    @Override
    public Request getRequestOrNotFound(Long requestId) {
        Optional<Request> request = requestRepository.findById(requestId);

        if (request.isEmpty()) {
            throw new RequestNotOwnerException(String.format("Заявка на участие c id %s не найдено", requestId));
        } else {
            return request.get();
        }
    }

    @Override
    public Compilation getCompilationOrNotFound(Long compId) {
        Optional<Compilation> compilation = compilationRepository.findById(compId);

        if (compilation.isEmpty()) {
            throw new CompilationNotFoundException(String.format("Подборка событий с id %s не найдена", compId));
        } else {
            return compilation.get();
        }
    }

    @Override
    public Comment getCommentOrNotFound(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new CommentNotFoundException(String.format("Комментарий с id %s не найдена", comment));
        } else {
            return comment.get();
        }
    }

    @Override
    public Boolean userIsCommentCreator(User user, Comment comment) {
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new UserNotOwnerException(String.format("Пользователь c id %s не является создателем комментария с id %s",
                    comment.getId(), user.getId()));
        }
        return true;
    }

}