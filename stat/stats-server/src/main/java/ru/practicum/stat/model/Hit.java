package ru.practicum.stat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hits")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "app")
    private String app;

    @Column(nullable = false, name = "uri")
    private String uri;

    @Column(nullable = false, name = "ip")
    private String ip;

    @Column(nullable = false, name = "timestamp")
    private LocalDateTime timestamp;
}
