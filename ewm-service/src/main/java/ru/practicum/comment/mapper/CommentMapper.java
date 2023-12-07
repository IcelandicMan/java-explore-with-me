package ru.practicum.comment.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.comment.dto.CommentRequestDto;
import ru.practicum.comment.dto.CommentResponseAnswerDto;
import ru.practicum.comment.dto.CommentResponseParentDto;
import ru.practicum.comment.model.Comment;

import java.time.LocalDateTime;

import static ru.practicum.util.emum.State.PENDING;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static Comment commentRequestDtoToComment(CommentRequestDto commentRequestDto) {
        return Comment.builder()
                .text(commentRequestDto.getText())
                .created(LocalDateTime.now())
                .state(PENDING)
                .build();
    }

    public static CommentResponseParentDto commentToCommentResponseParentDto(Comment comment) {
        return CommentResponseParentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .state(comment.getState())
                .build();
    }

    public static CommentResponseAnswerDto commentToCommentResponseAnswerDto(Comment comment) {
        return CommentResponseAnswerDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .state(comment.getState())
                .build();
    }
}
