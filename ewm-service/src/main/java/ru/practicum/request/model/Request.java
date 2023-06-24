package ru.practicum.request.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import ru.practicum.user.model.User;
import ru.practicum.event.model.Event;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonFormat;

@Setter
@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request", schema = "public")

public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne()
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne()
    @JoinColumn(name = "requester_id")
    private User requester;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private Status status;
}