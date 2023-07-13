package ru.practicum.comments.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class CommentDtoUpdate {

    @NotBlank
    @Size(max = 1000)
    private String text;
}
