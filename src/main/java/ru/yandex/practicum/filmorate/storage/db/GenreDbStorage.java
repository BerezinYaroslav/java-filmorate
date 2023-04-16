package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

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
}
