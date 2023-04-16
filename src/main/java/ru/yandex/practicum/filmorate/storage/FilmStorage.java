package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    List<Film> getAllFilms();

    Optional<Film> getFilmById(Integer id);

    Optional<Film> deleteFilm(Integer filmId);

    Optional<Genre> getGenre(Integer genreId);

    List<Genre> getAllGenres();

    Film likeFilm(Integer filmId, Integer userId);

    Film unlikeFilm(Integer filmId, Integer userId);

    List<Film> getMostPopularFilm(Integer count);
}
