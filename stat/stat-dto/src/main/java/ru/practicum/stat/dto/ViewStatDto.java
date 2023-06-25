package ru.practicum.stat.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatDto {

    private String app;

    private String uri;

    private Long hits;
}
