package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {
    private final MpaStorage mpaStorage;

    @Test
    public void getAllMpa_expectedSize5() {
        List<Mpa> mpaRatingList = mpaStorage.getAllMpa();
        assertThat(mpaRatingList.size()).isEqualTo(5);
    }

    @Test
    public void getMpaById_expectedMpaNameG() {
        Mpa mpaExpected = new Mpa(1, "G");
        Mpa mpaRating = mpaStorage.getMpa(1).get();
        assertThat(mpaRating).isEqualTo(mpaExpected);
    }
}
