package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService service;

    @PostMapping
    private Film addFilm(@RequestBody Film film) {
        validate(film);
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validate(film);
        return service.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Integer filmId) {
        return service.getFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeFilm(@PathVariable Integer filmId,
                         @PathVariable Integer userId) {
        return service.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film unlikeFilm(@PathVariable Integer filmId,
                           @PathVariable Integer userId) {
        return service.unlikeFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostLikedFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return service.getMostLikedFilms(count);
    }

    public static void validate(Film film) {
        if (film.getName().isBlank()) {
            log.debug("Validation error: Films name can't be blank");
            throw new ValidationException("Films name can't be blank");
        }

        if (film.getDescription().length() > 200) {
            log.debug("Validation error: Films description can't be longer than 200 chars");
            throw new ValidationException("Films description can't be longer than 200 chars");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Validation error: Films release date can't be before 28.12.1895");
            throw new ValidationException("Films release date can't be before 28.12.1895");
        }

        if (film.getDuration() < 0) {
            log.debug("Validation error: Films duration can't be negative");
            throw new ValidationException("Films duration can't be negative");
        }
    }
}
