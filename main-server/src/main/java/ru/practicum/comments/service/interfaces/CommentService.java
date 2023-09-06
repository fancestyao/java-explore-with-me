package ru.practicum.comments.service.interfaces;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentInputDto;
import ru.practicum.comments.dto.UpdatedCommentDto;
import java.util.List;

public interface CommentService {
    CommentDto saveComment(CommentInputDto commentInputDto, Long userId, Long eventId);

    UpdatedCommentDto updateCommentByUser(UpdatedCommentDto updatedCommentDto, Long userId, Long commentId);

    UpdatedCommentDto updateCommentByAdmin(UpdatedCommentDto updatedCommentDto, Long commentId);

    void deleteCommentByUser(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);

    CommentDto getCommentById(Long commentId);

    CommentDto getCommentByIdForUser(Long commentId, Long userId);

    List<CommentDto> getAllCommentsByEventForUser(Long eventId, Long userId, int from, int size);

    List<CommentDto> getAllComments(int from, int size);

    List<CommentDto> getAllCommentsByEvent(Long eventId, int from, int size);
}
