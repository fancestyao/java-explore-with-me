package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentInputDto;
import ru.practicum.comments.dto.UpdatedCommentDto;
import ru.practicum.comments.service.classes.CommentServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
@Slf4j
public class CommentController {
    private final CommentServiceImpl commentServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto saveComment(@RequestBody @Valid CommentInputDto commentInputDto,
                                  @RequestParam Long userId,
                                  @RequestParam Long eventId) {
        log.info("Поступил запрос на контроллер CommentController" +
                " на создание комментария для пользователя с id: {}," +
                " на событие с id: {}, commentInputDto: {}", userId, eventId, commentInputDto);
        return commentServiceImpl.saveComment(commentInputDto, userId, eventId);
    }

    @PatchMapping("/{commentId}")
    public UpdatedCommentDto updateComment(@RequestBody @Valid UpdatedCommentDto updatedCommentDto,
                                          @RequestParam Long userId,
                                          @PathVariable Long commentId) {
        log.info("Поступил запрос на контроллер CommentController" +
                " на обновление комментария с id: {} для пользователя с id: {}," +
                " updatedCommentDto: {}", commentId, userId, updatedCommentDto);
        return commentServiceImpl.updateComment(updatedCommentDto, userId, commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@RequestParam Long userId,
                              @PathVariable Long commentId) {
        log.info("Поступил запрос на контроллер CommentController" +
                " на удаление комментария с id: {} для пользователя с id: {}", commentId, userId);
        commentServiceImpl.deleteComment(userId, commentId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        log.info("Поступил запрос на контроллер CommentController" +
                " на получение комментария с id: {}", commentId);
        return commentServiceImpl.getCommentById(commentId);
    }

    @GetMapping
    public List<CommentDto> getAllComments() {
        log.info("Поступил запрос на контроллер CommentController" +
                " на получение всех комментариев");
        return commentServiceImpl.getAllComments();
    }

    @GetMapping("/events/{eventId}")
    public List<CommentDto> getAllCommentsByEvent(@PathVariable Long eventId) {
        log.info("Поступил запрос на контроллер CommentController" +
                " на получение всех комментариев для события с id: {}", eventId);
        return commentServiceImpl.getAllCommentsByEvent(eventId);
    }

    @GetMapping("/users/{userId}")
    public List<CommentDto> getAllCommentsByUser(@PathVariable Long userId) {
        log.info("Поступил запрос на контроллер CommentController" +
                " на получение всех комментариев от пользователя с id: {}", userId);
        return commentServiceImpl.getAllCommentsByUser(userId);
    }
}