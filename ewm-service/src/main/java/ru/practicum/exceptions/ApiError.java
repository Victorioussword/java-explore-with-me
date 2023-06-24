package ru.practicum.exceptions;

import lombok.*;
import java.util.List;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private List<String> errors;

    private HttpStatus status;

    private String reason;

    private String message;

    private String timestamp;
}