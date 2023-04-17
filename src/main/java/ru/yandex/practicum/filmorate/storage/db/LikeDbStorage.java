package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Component
@Primary
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;

    @Override
    public Film likeFilm(Integer filmId, Integer userId) {
        String insertLike = "INSERT INTO \"like_list\" (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(insertLike,
                filmId,
                userId);
        Film film = filmStorage.getFilmById(filmId).get();
        film.getLikesIds().add(userId);
        return film;
    }

    @Override
    public Film unlikeFilm(Integer filmId, Integer userId) {
        String deleteLike = "DELETE FROM \"like_list\" WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(deleteLike,
                filmId,
                userId);
        Film film = filmStorage.getFilmById(filmId).get();
        film.getLikesIds().remove(userId);
        return film;
    }
}
