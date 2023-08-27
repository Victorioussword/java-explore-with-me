package ru.practicum.comments.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.dto.CommentDtoInput;
import ru.practicum.comments.dto.CommentDtoOutput;
import ru.practicum.comments.model.Comment;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public CommentDtoOutput toCommentDtoOutput(Comment comment) {
        return new CommentDtoOutput(
                comment.getId(),
                comment.getText(),
                comment.getCreator().getId(),
                comment.getEvent().getId(),
                comment.getCreateTime(),
                comment.getUpdateTime()

        );
    }

    public Comment toComment(CommentDtoInput commentDtoInput) {
        return new Comment(
                0L,
                commentDtoInput.getText(),
                null,
                null,
                LocalDateTime.now(),
                null);
    }
}