package ru.practicum.stat.dto;

//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class InputHitDto {


    @NotNull
    @NotBlank
    private String app;

    @NotNull
    @NotBlank
    private String uri;

    @NotNull
    @NotBlank
    private String ip;

    @NotNull
    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    //2023-05-31 15:08:34
}

//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
////    String date = "12.12.1990";

//    LocalDate localDate = LocalDate.parse(date, formatter);