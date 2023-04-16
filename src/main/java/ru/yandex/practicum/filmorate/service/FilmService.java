package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;

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

    public Film deleteFilm(Integer filmId) {
        Film film = getFilmById(filmId);
        Optional<Film> optionalFilm = filmStorage.deleteFilm(filmId);

        if (optionalFilm.isEmpty()) {
            log.debug("Incorrect ID error: Film with this ID does not exist when deleting");
            throw new NotFoundException("Film with this ID does not exist when deleting");
        }

        log.debug("Film '" + film.getName() + "' delete successfully");
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

    public Genre getGenre(Integer id) {
        Optional<Genre> genre = filmStorage.getGenre(id);

        if (genre.isEmpty()) {
            throw new NotFoundException("Genre with id " + id + " not found");
        }

        return genre.get();
    }

    public Collection<Genre> getAllGenres() {
        List<Genre> genreList = filmStorage.getAllGenres();

        if (genreList.isEmpty()) {
            throw new NotFoundException("Genres not found");
        }

        return genreList;
    }

    public Film likeFilm(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        Optional<User> optionalUser = userStorage.getUserById(userId);

        if (optionalUser.isEmpty()) {
            log.debug("Incorrect ID error: User with this ID does not exist when liking film");
            throw new NotFoundException("User with this ID does not exist when liking film");
        }

        filmStorage.likeFilm(filmId, userId);
        log.debug("Film with ID '" + filmId + "' successfully liked by user with ID '" + userId + "'");
        return film;
    }

    public Film unlikeFilm(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);

        if (!film.getLikesIds().contains(userId)) {
            log.debug("Incorrect Argument error: Film '" + film + "' has not likes from user with ID '" + userId + "' when unliking");
            throw new NotFoundException("Film '" + film + "' has not likes from user with ID '" + userId + "' when unliking");
        }

        filmStorage.unlikeFilm(filmId, userId);
        log.debug("Film with id '" + filmId + "' successfully unliked by user with ID '" + userId + "'");
        return film;
    }

    public List<Film> getMostLikedFilms(Integer count) {
//        List<Film> films = new ArrayList<>(filmStorage.getAllFilms());
//
//        log.debug("Most popular films returned successfully");
//        return films.stream()
//                .sorted((f1, f2) -> -1 * (f1.getLikesIds().size() - f2.getLikesIds().size()))
//                .limit(count)
//                .collect(Collectors.toList());

        return filmStorage.getMostPopularFilm(count);
    }

    public Mpa getMpa(Integer mpaId) {
        log.debug("Mpa returned");

        if (mpaStorage.getMpa(mpaId) != null) {
            return mpaStorage.getMpa(mpaId);
        } else {
            throw new NotFoundException("Mpa with id " + mpaId + " not found");
        }
    }

    public List<Mpa> getAllMpa() {
        log.debug("All mpa returned");
        return mpaStorage.getAllMpa();
    }
}
