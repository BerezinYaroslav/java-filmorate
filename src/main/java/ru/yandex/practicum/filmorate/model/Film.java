package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private Integer id;
    private String name;
    private String description;
    private String genre;
    private String rating;
    private LocalDate releaseDate;
    private Integer duration;
    private final Set<Integer> likesIds = new HashSet<>();
}
