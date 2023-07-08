package ru.practicum.category.model;

import lombok.*;

import javax.persistence.*;

import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Size;

@Entity
@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 50)
    @Column(name = "name", unique = true, nullable = false)
    private String name;
}