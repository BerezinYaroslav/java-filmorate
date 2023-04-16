package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> deleteFilm(Integer filmId) {
        return Optional.empty();
    }

    // TODO: 14.04.2023 убрать
    @Override
    public Optional<Genre> getGenre(Integer genreId) {
        return Optional.empty();
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            return Optional.empty();
        }

        films.put(film.getId(), film);
        return Optional.of(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        if (!films.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(films.get(id));
    }

    @Override
    public Film likeFilm(Integer filmId, Integer userId) {
        return null;
    }

    @Override
    public Film unlikeFilm(Integer filmId, Integer userId) {
        return null;
    }

    @Override
    public List<Film> getMostPopularFilm(Integer count) {
        return null;
    }
}
