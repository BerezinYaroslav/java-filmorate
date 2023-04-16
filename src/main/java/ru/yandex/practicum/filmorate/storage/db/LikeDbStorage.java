package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;

    @Override
    public Film likeFilm(Integer filmId, Integer userId) {
        String sql = "INSERT INTO \"like_list\" (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql,
                filmId,
                userId);
        Film film = filmStorage.getFilmById(filmId).get();
        film.getLikesIds().add(userId);
        return film;
    }

    @Override
    public Film unlikeFilm(Integer filmId, Integer userId) {
        String sql = "DELETE FROM \"like_list\" WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql,
                filmId,
                userId);
        Film film = filmStorage.getFilmById(filmId).get();
        film.getLikesIds().remove(userId);
        return film;
    }

    @Override
    public List<Film> getMostPopularFilm(Integer count) {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"film\" GROUP BY ID " +
                "ORDER BY COUNT(SELECT * FROM \"like_list\" WHERE ID = \"film\".ID), ID DESC");
        int i = 0;

        while (filmRows.next() && i < count) {
            films.add(filmStorage.getFilmById(filmRows.getInt("id")).get());
            i++;
        }

        return films;
    }
}
