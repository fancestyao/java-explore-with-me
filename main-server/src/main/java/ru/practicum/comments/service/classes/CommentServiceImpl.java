package ru.practicum.comments.service.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentInputDto;
import ru.practicum.comments.dto.UpdatedCommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.comments.service.interfaces.CommentService;
import ru.practicum.event.enumerated.State;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.comments.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto saveComment(CommentInputDto commentInputDto, Long userId, Long eventId) {
        log.info("Запрос на создание комментария успешно передан в сервис CommentServiceImpl");
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не существует"));
        Comment comment;
        if (event.getState().equals(State.PUBLISHED)) {
            comment = commentMapper.commentInputDtoToComment(commentInputDto);
            comment.setCreated(LocalDateTime.now());
            comment.setUpdated(null);
            comment.setUser(user);
            comment.setEvent(event);
            commentRepository.save(comment);
        } else {
            throw new ConflictException("Комментарии можно добавлять только к опубликованным событиям");
        }
        log.info("Комментарий: {} успешно сохранен", comment);
        return commentMapper.commentToCommentDto(comment);
    }

    @Override
    public UpdatedCommentDto updateCommentByUser(UpdatedCommentDto updatedCommentDto, Long userId, Long commentId) {
        log.info("Запрос на обновление комментария успешно передан в сервис CommentServiceImpl");
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не существует"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " не существует"));
        if (!comment.getUser().getId().equals(userId)) {
            throw new BadRequestException("Пользователь не является автором комментария");
        }
        return updateComment(updatedCommentDto, comment);
    }

    @Override
    public UpdatedCommentDto updateCommentByAdmin(UpdatedCommentDto updatedCommentDto, Long commentId) {
        log.info("Запрос на обновление комментария от админа успешно передан в сервис CommentServiceImpl");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " не существует"));
        return updateComment(updatedCommentDto, comment);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        log.info("Запрос на удаление комментария от админа успешно передан в сервис CommentServiceImpl");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " не существует"));
        commentRepository.delete(comment);
        log.info("Комментарий с id: {} успешно удален админом", commentId);
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        log.info("Запрос на получение комментария успешно передан в сервис CommentServiceImpl");
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с id: " + commentId + " не существует"));
        log.info("Успешно получен комментарий с id: {}", commentId);
        return commentMapper.commentToCommentDto(comment);
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        log.info("Запрос на удаление комментария пользователем успешно передан в сервис CommentServiceImpl");
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не существует"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " не существует"));
        if (!comment.getUser().getId().equals(userId)) {
            throw new ConflictException("Данный пользователь не имеет прав удалять комментарий");
        }
        commentRepository.delete(comment);
        log.info("Комментарий с id: {} успешно удален пользователем с id: {}", commentId, userId);
    }

    @Override
    public CommentDto getCommentByIdForUser(Long commentId, Long userId) {
        log.info("Запрос на получение комментария для пользователя успешно передан в сервис CommentServiceImpl");
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не существует"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с id: " + commentId + " не существует"));
        log.info("Успешно получен комментарий с id: {}", commentId);
        return commentMapper.commentToCommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllCommentsByEvent(Long eventId, int from, int size) {
        log.info("Запрос на получение всех комментариев для события с id: {} " +
                "успешно передан в сервис CommentServiceImpl", eventId);
        return getComments(eventId, from, size);
    }

    @Override
    public List<CommentDto> getAllComments(int from, int size) {
        log.info("Запрос на получение всех комментариев успешно передан в сервис CommentServiceImpl");
        PageRequest pagination = PageRequest.of(from / size, size);
        List<Comment> comments = commentRepository.findAll(pagination).getContent();
        log.info("Успешно получены комментарии: {}", comments);
        return comments.stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getAllCommentsByEventForUser(Long eventId, Long userId, int from, int size) {
        log.info("Запрос от пользователя с id: {} на получение всех комментариев для события с id: {} " +
                "успешно передан в сервис CommentServiceImpl", userId, eventId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не существует"));
        return getComments(eventId, from, size);
    }

    private UpdatedCommentDto updateComment(UpdatedCommentDto updatedCommentDto, Comment comment) {
        if (!updatedCommentDto.getContent().equals(comment.getContent())) {
            comment.setContent(updatedCommentDto.getContent());
        } else {
            throw new ConflictException("Комментарий для обновления является идентичным настоящему");
        }
        comment.setUpdated(LocalDateTime.now());
        commentRepository.save(comment);
        log.info("Комментарий успешно обновлен на: {}", comment);
        return commentMapper.commentToUpdatedCommentDto(comment);
    }

    private List<CommentDto> getComments(Long eventId, int from, int size) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не существует"));
        PageRequest pagination = PageRequest.of(from / size, size);
        List<Comment> comments = commentRepository.findAllByEventId(eventId, pagination);
        log.info("Успешно получены комментарии: {}", comments);
        return comments.stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }
}