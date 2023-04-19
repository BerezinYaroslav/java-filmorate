package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

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
            if (!film.getGenres().isEmpty()) {
                addGenresToFilm(filmRows.getInt("id"), film.getGenres());
                film.setGenres(getFilmGenres(filmRows.getInt("id")));
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
                addGenresToFilm(film.getId(), film.getGenres());
            }
        }

        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlRequest = "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, " +
                "m.ID m_id, m.NAME m_name, fg.GENRE_ID fg_genre_id, g.NAME g_name, l.USER_ID l_user_id " +
                "FROM \"film\" f " +
                "LEFT JOIN \"mpa\" m ON f.MPA_ID = m.ID " +
                "LEFT JOIN \"film_genre\" fg ON f.ID = fg.FILM_ID " +
                "LEFT JOIN \"genre\" g ON fg.GENRE_ID = g.ID " +
                "LEFT JOIN \"like_list\" l ON f.ID = l.FILM_ID";
        return jdbcTemplate.query(sqlRequest, filmsExtractor);
    }

    private final ResultSetExtractor<List<Film>> filmsExtractor = rs -> {
        Map<Integer, Film> filmMap = new HashMap<>();
        Film film;

        while (rs.next()) {
            Integer filmId = rs.getInt("id");
            film = filmMap.get(filmId);

            if (film == null) {
                film = new Film(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        new Mpa(
                                rs.getInt("m_id"),
                                rs.getString("m_name")
                        )
                );
                filmMap.put(filmId, film);
            }

            Set<Genre> genres = film.getGenres();

            if (rs.getInt("genre_id") != 0) {
                genres.add(new Genre(
                        rs.getInt("genre_id"),
                        rs.getString("g_name")
                ));
            }

            Set<Integer> userLike = film.getLikesIds();

            if (rs.getInt("user_id") != 0) {
                userLike.add(rs.getInt("user_id"));
            }
        }

        return new ArrayList<>(filmMap.values());
    };

    @Override
    public Optional<Film> getFilmById(Integer id) {
        try {
            String sqlRequest = "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, " +
                    "f.DURATION, f.MPA_ID, m.ID m_id, m.NAME m_name FROM \"film\" f LEFT JOIN \"mpa\" m ON f.MPA_ID = m.ID " +
                    "WHERE f.ID = ?";
            Film film = jdbcTemplate.queryForObject(sqlRequest, new FilmRowMapper(), id);

            addLikesToFilm(film);
            film.setGenres(getFilmGenres(id));
            return Optional.of(film);
        } catch (Throwable e) {
            return Optional.empty();
        }
    }

    private void addLikesToFilm(Film film) {
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"like_list\" WHERE FILM_ID = ?",
                film.getId());

        while (likeRows.next()) {
            film.getLikesIds().add(likeRows.getInt("user_id"));
        }
    }

    private Set<Genre> getFilmGenres(int filmId) {
        Set<Genre> genres = new HashSet<>();
        SqlRowSet filmGenreRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"film_genre\" WHERE FILM_ID = ? ORDER BY GENRE_ID",
                filmId);

        while (filmGenreRows.next()) {
            Optional<Genre> genre = genreStorage.getGenre(filmGenreRows.getInt("genre_id"));
            genre.ifPresent(genres::add);
        }

        return genres;
    }

    private void addGenresToFilm(Integer filmId, Set<Genre> genres) {
        String insertGenre = "INSERT INTO \"film_genre\" (FILM_ID, GENRE_ID) VALUES (?, ?)";

        for (Genre genre : genres) {
            jdbcTemplate.update(insertGenre,
                    filmId,
                    genre.getId());
        }
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
