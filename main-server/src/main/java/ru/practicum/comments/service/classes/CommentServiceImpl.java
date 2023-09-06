package ru.practicum.comments.service.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentInputDto;
import ru.practicum.comments.dto.UpdatedCommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.comments.service.interfaces.CommentService;
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

    public CommentDto saveComment(CommentInputDto commentInputDto, Long userId, Long eventId) {
        log.info("Запрос на создание комментария успешно передан в сервис CommentServiceImpl");
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не существует"));
        commentInputDto.setCreated(LocalDateTime.now());
        Comment comment = commentMapper.commentInputDtoToComment(commentInputDto);
        comment.setUpdated(null);
        comment.setUser(user);
        comment.setEvent(event);
        commentRepository.save(comment);
        log.info("Комментарий: {} успешно сохранен", comment);
        return commentMapper.commentToCommentDto(comment);
    }

    public UpdatedCommentDto updateComment(UpdatedCommentDto updatedCommentDto, Long userId, Long commentId) {
        log.info("Запрос на обновление комментария успешно передан в сервис CommentServiceImpl");
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не существует"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " не существует"));
        if (!comment.getUser().getId().equals(userId)) {
            throw new BadRequestException("Пользователь не является автором комментария");
        }
        if (!updatedCommentDto.getContent().equals(comment.getContent())) {
            comment.setContent(updatedCommentDto.getContent());
        } else {
            throw new ConflictException("Комментарий для обновления является идентичным настоящему");
        }
        comment.setUpdated(LocalDateTime.now());
        commentRepository.save(comment);
        log.info("Комментарий успешно обновлена на: {}", comment);
        return commentMapper.commentToUpdatedCommentDto(comment);
    }

    public void deleteComment(Long userId, Long commentId) {
        log.info("Запрос на удаление комментария успешно передан в сервис CommentServiceImpl");
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не существует"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " не существует"));
        if (!comment.getUser().getId().equals(userId)) {
            throw new ConflictException("Данный пользователь не имеет прав удалять комментарий");
        }
        commentRepository.delete(comment);
        log.info("Комментарий с id: {} успешно удален", commentId);
    }

    public CommentDto getCommentById(Long commentId) {
        log.info("Запрос на получение комментария успешно передан в сервис CommentServiceImpl");
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с id: " + commentId + " не существует"));
        log.info("Успешно получен комментарий с id: {}", commentId);
        return commentMapper.commentToCommentDto(comment);
    }

    public List<CommentDto> getAllComments() {
        log.info("Запрос на получение всех комментариев успешно передан в сервис CommentServiceImpl");
        List<Comment> comments = commentRepository.findAll();
        log.info("Успешно получены комментарии: {}", comments);
        return comments.stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsByEvent(Long eventId) {
        log.info("Запрос на получение всех комментариев для события с id: {} " +
                "успешно передан в сервис CommentServiceImpl", eventId);
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не существует"));
        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        log.info("Успешно получены комментарии: {}", comments);
        return comments.stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsByUser(Long userId) {
        log.info("Запрос на получение всех комментариев для пользователя с id: {} " +
                "успешно передан в сервис CommentServiceImpl", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не существует"));
        List<Comment> comments = commentRepository.findAllByUserId(userId);
        log.info("Успешно получены комментарии: {}", comments);
        return comments.stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }
}