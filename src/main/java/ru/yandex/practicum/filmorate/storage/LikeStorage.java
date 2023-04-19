package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface LikeStorage {
    Film likeFilm(Integer filmId, Integer userId);

    Film unlikeFilm(Integer filmId, Integer userId);

    void clearLikes(Integer filmId);
}
