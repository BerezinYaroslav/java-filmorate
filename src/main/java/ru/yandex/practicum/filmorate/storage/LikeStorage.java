package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {
    Film likeFilm(Integer filmId, Integer userId);

    Film unlikeFilm(Integer filmId, Integer userId);

    List<Film> getMostPopularFilm(Integer count);
}
