package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.practicum.category.model.Category;
import ru.practicum.utils.enums.State;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "events", schema = "public")
@SuperBuilder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(length = 2000)
    String annotation;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    Category category;

    @Column(name = "created_on")
    LocalDateTime createdOn;

    @Column(name = "description", length = 7000)
    String description;

    @Column(name = "event_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @ManyToOne()
    @JoinColumn(name = "initiator_id")
    User initiator;

    @Embedded
    Location location;

    Boolean paid;

    @Column(name = "participant_limit")
    Long participantLimit;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    State state;

    @Column(name = "title")
    String title;
}
