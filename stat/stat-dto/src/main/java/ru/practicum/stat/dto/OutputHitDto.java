package ru.practicum.stat.dto;

import lombok.*;


@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutputHitDto {

    private String app;

    private String uri;


    private String ip;

    private String timestamp;
}