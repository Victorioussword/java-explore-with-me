package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoOutput {

    private long id;

    private String text;

    private Long creator;

    private Long event;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}