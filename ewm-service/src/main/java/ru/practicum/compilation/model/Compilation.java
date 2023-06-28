package ru.practicum.compilation.model;

import lombok.*;

import java.util.List;
import java.util.Set;
import javax.persistence.*;

import ru.practicum.event.model.Event;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilation")

public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToMany
    @JoinTable(name = "compilation_and_event",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;  // todo стало Set<Event>

    private Boolean pinned;
}
