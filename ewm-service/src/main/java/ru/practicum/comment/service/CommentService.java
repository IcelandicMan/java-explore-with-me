package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentRequestDto;
import ru.practicum.comment.dto.CommentResponseParentDto;
import ru.practicum.comment.dto.CommentUpdateAdminDto;

import java.util.List;

public interface CommentService {

    CommentResponseParentDto createComment(Long userId, Long eventId, CommentRequestDto comment);

    void deleteCommentByOwner(Long commentId, Long userId);

    void deleteCommentByAdmin(Long commentId);

    CommentResponseParentDto updateCommentByOwner(Long commentId, Long userId, CommentRequestDto commentRequestDto);

    List<CommentResponseParentDto> updateCommentByAdmin(CommentUpdateAdminDto commentUpdateAdminDto);

    List<CommentResponseParentDto> getAllCommentsForAdminAndPrivate(Long eventId);

    List<CommentResponseParentDto> getAllCommentsForPublic(Long eventId);

    public CommentResponseParentDto getCommentById(Long commentId);
}

