package ru.practicum.comments.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDtoInput;
import ru.practicum.comments.dto.CommentDtoOutput;
import ru.practicum.comments.dto.CommentDtoUpdate;
import ru.practicum.comments.service.CommentsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentsController {

    private final CommentsService commentsService;

    @PostMapping()
    public CommentDtoOutput createComment(
            @RequestBody @Valid CommentDtoInput commentDtoInput,
            @RequestParam Long creatorId,
            @RequestParam Long eventId) {

        CommentDtoOutput commentDtoForReturn = commentsService.create(commentDtoInput, creatorId, eventId);
        log.info("CommentService - createComment.  Создан Comment {} ", commentDtoForReturn.toString());
        return commentDtoForReturn;
    }


    @DeleteMapping("/{commentId}/user/{userId}")
    public void delComment(@PathVariable Long commentId,
                           @PathVariable Long userId) {


        commentsService.delComment(commentId, userId);
        log.info("CommentService - delComment().  Удален Comment # {} ", userId);
    }

    @GetMapping("/event/{eventId}")
    public List<CommentDtoOutput> getCommentsByEvent(

            @PathVariable Long eventId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        List<CommentDtoOutput> commentDtoOutputs = commentsService.getComments(eventId, from, size);
        log.info("CommentService - getComments().  Возвращен список из {} элеменов ", commentDtoOutputs.size());
        return commentDtoOutputs;
    }


    @GetMapping("/user/{userId}")
    public List<CommentDtoOutput> getCommentsByUser(
            @PathVariable Long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        List<CommentDtoOutput> commentDtoOutputs = commentsService.getCommentsByUser(userId, from, size);
        log.info("CommentService - getComments().  Возвращен список из {} элеменов ", commentDtoOutputs.size());
        return commentDtoOutputs;
    }


    @PatchMapping("/{commentId}/user/{userId}")
    public CommentDtoOutput updateComment(@RequestBody @Valid CommentDtoUpdate commentDtoUpdate,
                                          @PathVariable Long commentId,
                                          @PathVariable Long userId) {

        CommentDtoOutput commentDtoOutput = commentsService.update(commentDtoUpdate, commentId, userId);
        log.info("CommentService - updateComment().  Обновлен комметнарий {}", commentDtoOutput.toString());
        return commentDtoOutput;
    }
}