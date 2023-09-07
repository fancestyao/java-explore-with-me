package ru.practicum.comments.mapper;

import org.mapstruct.Mapper;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.dto.CommentInputDto;
import ru.practicum.comments.dto.UpdatedCommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment commentInputDtoToComment(CommentInputDto commentInputDto);

    UpdatedCommentDto commentToUpdatedCommentDto(Comment comment);

    CommentDto commentToCommentDto(Comment comment);
}
