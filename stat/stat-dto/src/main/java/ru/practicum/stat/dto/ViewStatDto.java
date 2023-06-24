package ru.practicum.stat.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatDto {

    private String app;

    private String uri;

    private Long hits;
}
