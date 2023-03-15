package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addFilm(Film film) {
        log.debug("Film '" + film.getName() + "' added successfully");
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        Optional<Film> optionalFilm = filmStorage.updateFilm(film);

        if (optionalFilm.isEmpty()) {
            log.debug("Incorrect ID error: Film with this ID does not exist when updating");
            throw new NotFoundException("Film with this ID does not exist when updating");
        }

        log.debug("Film '" + film.getName() + "' updated successfully");
        return optionalFilm.get();
    }

    public List<Film> getAllFilms() {
        log.debug("All users returned successfully");
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer filmId) {
        Optional<Film> optionalFilm = filmStorage.getFilmById(filmId);

        if (optionalFilm.isEmpty()) {
            log.debug("Incorrect ID error: Film with this ID does not exist when getting by ID");
            throw new NotFoundException("Film with this ID does not exist when getting by ID");
        }

        log.debug("Film with ID '" + filmId + "' returned successfully");
        return optionalFilm.get();
    }

    public Film likeFilm(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        Optional<User> optionalUser = userStorage.getUserById(userId);

        if (optionalUser.isEmpty()) {
            log.debug("Incorrect ID error: User with this ID does not exist when liking film");
            throw new NotFoundException("User with this ID does not exist when liking film");
        }

        film.getLikesIds().add(userId);
        log.debug("Film with ID '" + filmId + "' successfully liked by user with ID '" + userId + "'");
        return film;
    }

    public Film unlikeFilm(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);

        if (!film.getLikesIds().contains(userId)) {
            log.debug("Incorrect Argument error: Film '" + film + "' has not likes from user with ID '" + userId + "' when unliking");
            throw new NotFoundException("Film '" + film + "' has not likes from user with ID '" + userId + "' when unliking");
        }

        film.getLikesIds().remove(userId);
        log.debug("Film with id '" + filmId + "' successfully unliked by user with ID '" + userId + "'");
        return film;
    }

    public List<Film> getMostLikedFilms(Integer count) {
        List<Film> films = new ArrayList<>(getAllFilms());

        log.debug("Most popular films returned successfully");
        return films.stream()
                .sorted(this::compare)
                .skip(0)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return -1 * (f0.getLikesIds().size() - f1.getLikesIds().size());
    }
}
