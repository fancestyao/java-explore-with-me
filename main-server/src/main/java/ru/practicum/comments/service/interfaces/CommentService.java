package ru.practicum.comments.service.interfaces;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentInputDto;
import ru.practicum.comments.dto.UpdatedCommentDto;
import java.util.List;

public interface CommentService {
    CommentDto saveComment(CommentInputDto commentInputDto, Long userId, Long eventId);

    UpdatedCommentDto updateComment(UpdatedCommentDto updatedCommentDto, Long userId, Long commentId);

    void deleteComment(Long userId, Long commentId);

    CommentDto getCommentById(Long commentId);

    List<CommentDto> getAllComments();

    List<CommentDto> getAllCommentsByEvent(Long eventId);

    List<CommentDto> getAllCommentsByUser(Long userId);
}
