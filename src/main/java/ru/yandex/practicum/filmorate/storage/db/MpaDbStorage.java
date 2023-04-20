package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> getMpa(Integer mpaId) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"mpa\" WHERE id = ?", mpaId);

        if (mpaRows.next()) {
            return Optional.of(new Mpa(
                    mpaRows.getInt("id"),
                    mpaRows.getString("name")
            ));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        List<Mpa> mpaList = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"mpa\" ORDER BY ID");

        while (mpaRows.next()) {
            mpaList.add(new Mpa(
                    mpaRows.getInt("id"),
                    mpaRows.getString("name")
            ));
        }

        return mpaList;
    }
}
