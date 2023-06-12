package ru.practicum.ewmservice.request;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmservice.request.dto.OutputRequestDto;
import ru.practicum.ewmservice.request.model.Request;

@UtilityClass
public class RequestMapper {

    public static OutputRequestDto toOutputRequestDto(Request request) {
        return new OutputRequestDto(request.getId(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus(),
                request.getCreated());
    }

}


///  private Long id;
//
//    private Long event;
//
//    private Long requester;
//
//    private RequestStatus status;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime created;