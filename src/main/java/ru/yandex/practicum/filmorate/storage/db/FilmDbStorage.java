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
import java.util.stream.Collectors;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO \"film\" (NAME, DESCRIPTION, MPA_ID, RELEASE_DATE, DURATION) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDuration());

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"film\" WHERE NAME = ?", film.getName());

        if (filmRows.next()) {
            if (!film.getLikesIds().isEmpty()) {
                for (Integer userId : film.getLikesIds()) {
                    String sql1 = "INSERT INTO \"like_list\" (FILM_ID, USER_ID) VALUES (?, ?)";
                    jdbcTemplate.update(sql1,
                            filmRows.getInt("id"),
                            userId);
                }
            }

            if (!film.getGenres().isEmpty()) {
                String sql1 = "INSERT INTO \"film_genre\" (FILM_ID, GENRE_ID) VALUES (?, ?)";
                Set<Genre> genres = new HashSet<>();

                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update(sql1,
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

        String sql = "DELETE FROM \"film\" WHERE ID = ?";
        jdbcTemplate.update(sql, filmId);
        return film;
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (getFilmById(film.getId()).isEmpty()) {
            return Optional.empty();
        }

        String sql = "UPDATE \"film\" SET NAME = ?, RELEASE_DATE = ?, DESCRIPTION = ?, DURATION = ?, MPA_ID = ? WHERE ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"film\" WHERE ID = ?", film.getId());

        if (filmRows.next()) {
            String sql0 = "DELETE FROM \"film_genre\" WHERE FILM_ID = ?";
            jdbcTemplate.update(sql0,
                    film.getId());

            if (!film.getGenres().isEmpty()) {
                for (Genre genre : film.getGenres()) {
                    String sql1 = "INSERT INTO \"film_genre\" (FILM_ID, GENRE_ID)  VALUES (?, ?)";
                    jdbcTemplate.update(sql1,
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
            SqlRowSet mpaRows1 = jdbcTemplate.queryForRowSet("SELECT * from \"film\" WHERE ID = ?",
                    filmRows.getInt("id"));

            if (mpaRows1.next()) {
                SqlRowSet mpaRows2 = jdbcTemplate.queryForRowSet("SELECT * from \"mpa\" WHERE ID = ?",
                        mpaRows1.getInt("mpa_id"));

                if (mpaRows2.next()) {
                    mpa = new Mpa(
                            mpaRows2.getInt("id"),
                            mpaRows2.getString("name")
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
            SqlRowSet filmGenre = jdbcTemplate.queryForRowSet("SELECT * FROM \"film_genre\" WHERE FILM_ID = ? ORDER BY GENRE_ID",
                    filmRows.getInt("id"));

            Set<Genre> genres = new HashSet<>();

            while (filmGenre.next()) {
                SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"genre\" WHERE ID = ?",
                        filmGenre.getInt("genre_id"));

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

    @Override
    public Optional<Genre> getGenre(Integer genreId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"genre\" WHERE ID = ?", genreId);

        if (filmRows.next()) {
            return Optional.of(new Genre(
                    filmRows.getInt("id"),
                    filmRows.getString("name")
            ));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"genre\" ORDER BY ID");

        while (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name")
            );

            genres.add(genre);
        }

        return genres;
    }

    @Override
    public Film likeFilm(Integer filmId, Integer userId) {
        String sql = "INSERT INTO \"like_list\" (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql,
                filmId,
                userId);
        Film film = getFilmById(filmId).get();
        film.getLikesIds().add(userId);
        return film;
    }

    @Override
    public Film unlikeFilm(Integer filmId, Integer userId) {
        String sql = "DELETE FROM \"like_list\" WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql,
                filmId,
                userId);
        Film film = getFilmById(filmId).get();
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
            films.add(getFilmById(filmRows.getInt("id")).get());
            i++;
        }

        return films;
    }
}
