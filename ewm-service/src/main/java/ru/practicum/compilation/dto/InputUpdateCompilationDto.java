package ru.practicum.compilation.dto;


import lombok.*;

import java.util.List;

import javax.validation.constraints.Size;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class InputUpdateCompilationDto {

    @Size(min = 1, max = 50)
    private String title;

    private List<Long> events;

    private Boolean pinned;
}