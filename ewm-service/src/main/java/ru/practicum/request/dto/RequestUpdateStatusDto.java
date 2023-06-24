package ru.practicum.request.dto;

import lombok.*;
import java.util.List;
import lombok.experimental.SuperBuilder;
import ru.practicum.request.model.Status;



@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class RequestUpdateStatusDto {

    private List<Long> requestIds;

    private Status status;
}