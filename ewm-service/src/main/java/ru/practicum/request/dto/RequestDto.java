package ru.practicum.request.dto;

import lombok.*;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import ru.practicum.request.model.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class RequestDto {

    private Long id;

    private Long event;

    private Long requester;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    private Status status;
}
