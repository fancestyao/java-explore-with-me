package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.UpdatedCommentDto;
import ru.practicum.comments.service.interfaces.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
@Slf4j
public class AdminCommentController {
    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public UpdatedCommentDto updateCommentByAdmin(@RequestBody @Valid UpdatedCommentDto updatedCommentDto,
                                                  @PathVariable Long commentId) {
        log.info("Поступил запрос на контроллер AdminCommentController" +
                " на обновление комментария с id: {} " +
                " updatedCommentDto: {}", commentId, updatedCommentDto);
        return commentService.updateCommentByAdmin(updatedCommentDto, commentId);
    }

    @GetMapping
    public List<CommentDto> getAllComments(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Поступил запрос на контроллер AdminCommentController" +
                " на получение всех комментариев");
        return commentService.getAllComments(from, size);
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByAdmin(@PathVariable Long commentId) {
        log.info("Поступил запрос на контроллер AdminCommentController" +
                " на удаление комментария с id: {}", commentId);
        commentService.deleteCommentByAdmin(commentId);
    }
}
