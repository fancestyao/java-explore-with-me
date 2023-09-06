package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.interfaces.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
@Slf4j
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        log.info("Поступил запрос на контроллер PublicCommentController" +
                " на получение комментария с id: {}", commentId);
        return commentService.getCommentById(commentId);
    }

    @GetMapping
    public List<CommentDto> getAllCommentsByEvent(@RequestParam Long eventId,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Поступил запрос на контроллер PublicCommentController" +
                " на получение всех комментариев для события с id: {}", eventId);
        return commentService.getAllCommentsByEvent(eventId, from, size);
    }
}
