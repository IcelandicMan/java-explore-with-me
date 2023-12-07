package ru.practicum.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.parentComment IS NULL AND c.event.id = :eventId ORDER BY c.created")
    List<Comment> findParentCommentsByEventIdAndAllState(@Param("eventId") Long eventId);

    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentId ORDER BY c.created")
    List<Comment> findChildCommentsByAllState(@Param("parentId") Long parentId);

    @Query("SELECT c FROM Comment c WHERE c.parentComment IS NULL " +
            "AND c.event.id = :eventId " +
            "AND c.state = 'PUBLISHED' " +
            "ORDER BY c.created")
    List<Comment> findParentPublishedCommentsByEventId(@Param("eventId") Long eventId);

    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentId " +
            "AND c.state = 'PUBLISHED' " +
            "ORDER BY c.created")
    List<Comment> findChildPublishedComments(@Param("parentId") Long parentId);

    @Query("SELECT c FROM Comment c WHERE c.id IN :commentsId " +
            "ORDER BY c.created")
    List<Comment> findAllCommentsById(@Param("commentsId") List<Long> commentsId);

}

