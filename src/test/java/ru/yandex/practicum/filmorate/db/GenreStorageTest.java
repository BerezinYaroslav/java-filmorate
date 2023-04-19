package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTest {
    private final GenreStorage genreStorage;

    @Test
    public void getAllGenres() {
        List<Genre> genres = genreStorage.getAllGenres();
        assertThat(genres.size()).isEqualTo(6);
    }

    @Test
    public void getGenreById() {
        Genre genreExpected = new Genre(1, "Комедия");
        Genre genre = genreStorage.getGenre(1).get();
        System.out.println(genre);
        assertThat(genre).isEqualTo(genreExpected);
    }
}
