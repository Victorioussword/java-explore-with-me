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

    private final String NAME_OF_CLASS = "CommentsController";
    private final String METHOD_DEL_COMMENT = " - delComment";
    private final String METHOD_CREATE_COMMENT = " - createComment";
    private final String METHOD_GET_COMMENTS_BY_EVENT = " - getCommentsByEvent";
    private final String METHOD_GET_COMMENT_BY_USER = " - getCommentsByUser";
    private final String METHOD_UPDATE_COMMENT = " - updateComment";


    @PostMapping()
    public CommentDtoOutput createComment(
            @RequestBody @Valid CommentDtoInput commentDtoInput,
            @RequestParam Long creatorId,
            @RequestParam Long eventId) {
        log.info(NAME_OF_CLASS + METHOD_CREATE_COMMENT);
        return commentsService.create(commentDtoInput, creatorId, eventId);
    }


    @DeleteMapping("/{commentId}/user/{userId}")
    public void delComment(@PathVariable Long commentId,
                           @PathVariable Long userId) {
        log.info(NAME_OF_CLASS + METHOD_DEL_COMMENT);
        commentsService.delComment(commentId, userId);
    }


    @GetMapping("/event/{eventId}")
    public List<CommentDtoOutput> getCommentsByEvent(

            @PathVariable Long eventId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info(NAME_OF_CLASS + METHOD_GET_COMMENTS_BY_EVENT);
        return commentsService.getComments(eventId, from, size);
    }


    @GetMapping("/user/{userId}")
    public List<CommentDtoOutput> getCommentsByUser(
            @PathVariable Long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info(NAME_OF_CLASS + METHOD_GET_COMMENT_BY_USER);
        return commentsService.getCommentsByUser(userId, from, size);
    }


    @PatchMapping("/{commentId}/user/{userId}")
    public CommentDtoOutput updateComment(@RequestBody @Valid CommentDtoUpdate commentDtoUpdate,
                                          @PathVariable Long commentId,
                                          @PathVariable Long userId) {
        log.info(NAME_OF_CLASS + METHOD_UPDATE_COMMENT);
        return commentsService.update(commentDtoUpdate, commentId, userId);
    }
}