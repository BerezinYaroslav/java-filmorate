package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        String insertFilm = "INSERT INTO \"film\" (NAME, DESCRIPTION, MPA_ID, RELEASE_DATE, DURATION) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertFilm,
                film.getName(),
                film.getDescription(),
                film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDuration());
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"film\" WHERE NAME = ?", film.getName());

        if (filmRows.next()) {
            if (!film.getLikesIds().isEmpty()) {
                for (Integer userId : film.getLikesIds()) {
                    String insertLike = "INSERT INTO \"like_list\" (FILM_ID, USER_ID) VALUES (?, ?)";
                    jdbcTemplate.update(insertLike,
                            filmRows.getInt("id"),
                            userId);
                }
            }

            if (!film.getGenres().isEmpty()) {
                String insertGenre = "INSERT INTO \"film_genre\" (FILM_ID, GENRE_ID) VALUES (?, ?)";
                Set<Genre> genres = new HashSet<>();

                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update(insertGenre,
                            filmRows.getInt("id"),
                            genre.getId());
                    SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"genre\" WHERE id = ?",
                            genre.getId());

                    if (genreRows.next()) {
                        genres.add(new Genre(
                                genreRows.getInt("id"),
                                genreRows.getString("name")
                        ));
                    }
                }

                film.setGenres(genres);
            }

            return getFilmById(filmRows.getInt("id")).get();
        } else {
            return film;
        }
    }

    @Override
    public Optional<Film> deleteFilm(Integer filmId) {
        Optional<Film> film = getFilmById(filmId);

        if (film.isEmpty()) {
            return Optional.empty();
        }

        String deleteFilm = "DELETE FROM \"film\" WHERE ID = ?";
        jdbcTemplate.update(deleteFilm, filmId);
        return film;
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (getFilmById(film.getId()).isEmpty()) {
            return Optional.empty();
        }

        String updateFilm = "UPDATE \"film\" SET NAME = ?, RELEASE_DATE = ?, DESCRIPTION = ?, DURATION = ?, MPA_ID = ? WHERE ID = ?";
        jdbcTemplate.update(updateFilm,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"film\" WHERE ID = ?", film.getId());

        if (filmRows.next()) {
            String deleteGenre = "DELETE FROM \"film_genre\" WHERE FILM_ID = ?";
            jdbcTemplate.update(deleteGenre,
                    film.getId());

            if (!film.getGenres().isEmpty()) {
                for (Genre genre : film.getGenres()) {
                    String insertGenre = "INSERT INTO \"film_genre\" (FILM_ID, GENRE_ID)  VALUES (?, ?)";
                    jdbcTemplate.update(insertGenre,
                            film.getId(),
                            genre.getId());
                }
            }
        }

        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"film\"");

        while (filmRows.next()) {
            Film film = getFilmById(filmRows.getInt("id")).get();
            films.add(film);
        }

        return films;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"film\" WHERE ID = ?", id);

        if (filmRows.next()) {
            Mpa mpa = null;
            SqlRowSet filmRows1 = jdbcTemplate.queryForRowSet("SELECT * from \"film\" WHERE ID = ?",
                    filmRows.getInt("id"));

            if (filmRows1.next()) {
                SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * from \"mpa\" WHERE ID = ?",
                        filmRows1.getInt("mpa_id"));

                if (mpaRows.next()) {
                    mpa = new Mpa(
                            mpaRows.getInt("id"),
                            mpaRows.getString("name")
                    );
                }
            }

            Film film = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration")
            );
            SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"like_list\" WHERE FILM_ID = ?",
                    film.getId());

            while (likeRows.next()) {
                film.getLikesIds().add(likeRows.getInt("user_id"));
            }

            film.setMpa(mpa);
            SqlRowSet filmGenreRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"film_genre\" WHERE FILM_ID = ? ORDER BY GENRE_ID",
                    filmRows.getInt("id"));
            Set<Genre> genres = new HashSet<>();

            while (filmGenreRows.next()) {
                SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"genre\" WHERE ID = ?",
                        filmGenreRows.getInt("genre_id"));

                if (genreRows.next()) {
                    genres.add(new Genre(
                            genreRows.getInt("id"),
                            genreRows.getString("name")
                    ));
                }
            }

            film.setGenres(genres);
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }
}
