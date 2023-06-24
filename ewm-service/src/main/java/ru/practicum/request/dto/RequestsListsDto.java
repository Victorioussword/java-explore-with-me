package ru.practicum.request.dto;

import lombok.*;
import java.util.List;
import lombok.experimental.SuperBuilder;


@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class RequestsListsDto {

    private List<RequestDto> confirmedRequests;

    private List<RequestDto> rejectedRequests;
}