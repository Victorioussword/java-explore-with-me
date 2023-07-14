package ru.practicum.comments.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 1000)
    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne()
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne()
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "create_Time")
    private LocalDateTime createTime;

    @Column(name = "update_Time")
    private LocalDateTime updateTime;
}