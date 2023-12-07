package ru.practicum.comment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentRequestDto;
import ru.practicum.comment.dto.CommentResponseAnswerDto;
import ru.practicum.comment.dto.CommentResponseParentDto;
import ru.practicum.comment.dto.CommentUpdateAdminDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;
import ru.practicum.util.emum.State;
import ru.practicum.util.exception.ConflictException;
import ru.practicum.util.service.UnionService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UnionService unionService;

    @Override
    public CommentResponseParentDto createComment(Long userId, Long eventId, CommentRequestDto comment) {
        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(eventId);

        Comment createdComment = CommentMapper.commentRequestDtoToComment(comment);
        Comment mainComment = null;

        if (!event.getRequestModeration()) {
            createdComment.setState(State.PUBLISHED);
        }

        if (comment.getAnswerToCommentId().isPresent()) {
            Long mainCommentId = comment.getAnswerToCommentId().get();
            mainComment = unionService.getCommentOrNotFound(mainCommentId);
        }

        createdComment.setAuthor(user);
        createdComment.setEvent(event);

        if (mainComment != null) {
            createdComment.setParentComment(mainComment);
        }
        return CommentMapper.commentToCommentResponseParentDto(commentRepository.save(createdComment));
    }

    @Override
    public void deleteCommentByOwner(Long commentId, Long userId) {
        Comment comment = unionService.getCommentOrNotFound(commentId);
        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(comment.getEvent().getId());
        unionService.userIsCommentCreator(user, comment);

        if (event.getRequestModeration() && !comment.getState().equals(State.PENDING)) {
            throw new ConflictException("Комментарий не должен быть опубликован или отклонен");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        unionService.getCommentOrNotFound(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentResponseParentDto updateCommentByOwner(Long commentId, Long userId, CommentRequestDto commentRequestDto) {
        Comment comment = unionService.getCommentOrNotFound(commentId);
        User user = unionService.getUserOrNotFound(userId);
        Event event = unionService.getEventOrNotFound(comment.getEvent().getId());
        unionService.userIsCommentCreator(user, comment);

        if (event.getRequestModeration() && !comment.getState().equals(State.PENDING)) {
            throw new ConflictException("Комментарий не должен быть опубликован или отклонен");
        }

        if (commentRequestDto.getText() != null) {
            comment.setText(commentRequestDto.getText());
        }

        if (commentRequestDto.getAnswerToCommentId().isPresent()) {
            Long parentCommentId = commentRequestDto.getAnswerToCommentId().get();
            Comment mainComment = unionService.getCommentOrNotFound(parentCommentId);
            comment.setParentComment(mainComment);
        }
        return CommentMapper.commentToCommentResponseParentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponseParentDto> updateCommentByAdmin(CommentUpdateAdminDto commentUpdateAdminDto) {
        List<Comment> comments = commentRepository.findAllCommentsById(commentUpdateAdminDto.getCommentsIds());

        for (Comment comment : comments) {
            comment.setState(commentUpdateAdminDto.getState());
            commentRepository.save(comment);
        }

        return comments.stream().map(CommentMapper::commentToCommentResponseParentDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseParentDto> getAllCommentsForAdminAndPrivate(Long eventId) {

        List<Comment> commentList = commentRepository.findParentCommentsByEventIdAndAllState(eventId);
        List<CommentResponseParentDto> commentResponse = new ArrayList<>();

        for (Comment comment : commentList) {
            List<Comment> childComments = commentRepository.findChildCommentsByAllState(comment.getId());
            List<CommentResponseAnswerDto> answers = childComments.stream()
                    .map(CommentMapper::commentToCommentResponseAnswerDto)
                    .collect(Collectors.toList());
            CommentResponseParentDto parentDto = CommentMapper.commentToCommentResponseParentDto(comment);
            parentDto.setAnswers(answers);
            commentResponse.add(parentDto);
        }
        return commentResponse;
    }

    @Override
    public List<CommentResponseParentDto> getAllCommentsForPublic(Long eventId) {

        List<Comment> commentList = commentRepository.findParentPublishedCommentsByEventId(eventId);
        List<CommentResponseParentDto> commentResponse = new ArrayList<>();

        for (Comment comment : commentList) {
            List<Comment> childComments = commentRepository.findChildPublishedComments(comment.getId());
            List<CommentResponseAnswerDto> answers = childComments.stream()
                    .map(CommentMapper::commentToCommentResponseAnswerDto)
                    .collect(Collectors.toList());
            CommentResponseParentDto parentDto = CommentMapper.commentToCommentResponseParentDto(comment);
            parentDto.setAnswers(answers);
            commentResponse.add(parentDto);
        }
        return commentResponse;
    }

    @Override
    public CommentResponseParentDto getCommentById(Long commentId) {
        Comment comment = unionService.getCommentOrNotFound(commentId);
        return CommentMapper.commentToCommentResponseParentDto(comment);
    }
}
