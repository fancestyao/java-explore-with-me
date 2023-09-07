package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentInputDto;
import ru.practicum.comments.dto.UpdatedCommentDto;
import ru.practicum.comments.service.interfaces.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
@Slf4j
public class UserCommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto saveComment(@RequestBody @Valid CommentInputDto commentInputDto,
                                  @PathVariable Long userId,
                                  @RequestParam Long eventId) {
        log.info("Поступил запрос на контроллер UserCommentController" +
                " на создание комментария для пользователя с id: {}," +
                " на событие с id: {}, commentInputDto: {}", userId, eventId, commentInputDto);
        return commentService.saveComment(commentInputDto, userId, eventId);
    }

    @PatchMapping
    public UpdatedCommentDto updateCommentByUser(@RequestBody @Valid UpdatedCommentDto updatedCommentDto,
                                                 @PathVariable Long userId) {
        log.info("Поступил запрос на контроллер UserCommentController" +
                " на обновление комментария с id: {} " +
                " updatedCommentDto: {} пользователем с id: {}", updatedCommentDto.getId(), updatedCommentDto, userId);
        return commentService.updateCommentByUser(updatedCommentDto, userId, updatedCommentDto.getId());
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByUser(@PathVariable Long commentId,
                                    @PathVariable Long userId) {
        log.info("Поступил запрос на контроллер UserCommentController" +
                " на удаление комментария с id: {} пользователем с id: {}", commentId, userId);
        commentService.deleteCommentByUser(userId, commentId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentByIdForUser(@PathVariable Long commentId,
                                            @PathVariable Long userId) {
        log.info("Поступил запрос на контроллер UserCommentController" +
                " на получение комментария с id: {}, для пользователя с id: {}", commentId, userId);
        return commentService.getCommentByIdForUser(commentId, userId);
    }

    @GetMapping
    public List<CommentDto> getAllCommentsByEvent(@RequestParam Long eventId,
                                                  @PathVariable Long userId,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Поступил запрос на контроллер UserCommentController" +
                " на получение всех комментариев для события с id: {}", eventId);
        return commentService.getAllCommentsByEventForUser(eventId, userId, from, size);
    }
}