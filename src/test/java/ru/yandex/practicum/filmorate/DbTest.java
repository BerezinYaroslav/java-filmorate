package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbTest {
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Test
    void testGetAllMpa_ExpectedSize5() {
        List<Mpa> mpaRatingList = mpaStorage.getAllMpa();
        assertThat(mpaRatingList.size()).isEqualTo(5);
    }

    @Test
    public void testGetMpaById_ExpectedMpaNameG() {
        Mpa mpaExpected = new Mpa(1, "G");
        Mpa mpaRating = mpaStorage.getMpa(1).get();
        assertThat(mpaRating).isEqualTo(mpaExpected);
    }

    @Test
    void testGetAllGenres_ExpectedSize6() {
        List<Genre> genres = genreStorage.getAllGenres();
        assertThat(genres.size()).isEqualTo(6);
    }

    @Test
    public void testGetGenreById_ExpectedGenreEquals() {
        Genre genreExpected = new Genre(1, "Комедия");
        Genre genre = genreStorage.getGenre(1).get();
        System.out.println(genre);
        assertThat(genre).isEqualTo(genreExpected);
    }

    @Test
    void checkWriteFormDb() {
        filmStorage.addFilm(Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(new HashSet<>())
                .mpa(new Mpa(
                        3,
                        "PG-13")
                )
                .build());
        filmStorage.addFilm(Film.builder()
                .name("test1")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(new HashSet<>())
                .mpa(new Mpa(
                        3,
                        "PG-13")
                )
                .build());
        filmStorage.addFilm(Film.builder()
                .name("test2")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(new HashSet<>())
                .mpa(new Mpa(
                        3,
                        "PG-13")
                )
                .build());

        List<Film> films = filmStorage.getAllFilms();
        assertThat(films.get(0).getName()).isEqualTo("TestCheck");
        assertThat(films.get(1).getDescription()).isEqualTo("test");
        assertThat(films.get(2).getId()).isEqualTo(3);
    }

    @Test
    void updateFilm_expectedCorrectUpdate() {
        filmStorage.addFilm(Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .mpa(new Mpa(
                        3,
                        "PG-13")
                )
                .genres(new HashSet<>())
                .build());
        Film film = filmStorage.getFilmById(1).get();
        film.setName("TestCheck");
        filmStorage.updateFilm(film);
        assertThat(filmStorage.getFilmById(1).get().getName()).isEqualTo("TestCheck");
    }
}
