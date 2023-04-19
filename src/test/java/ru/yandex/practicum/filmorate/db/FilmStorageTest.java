package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {
    private final FilmStorage filmStorage;
    private List<Film> films;

    @BeforeEach
    public void pullFilmDb() {
        clearFilmDb();

        filmStorage.addFilm(Film.builder()
                .name("test1")
                .description("test1")
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
                .description("test2")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(new HashSet<>())
                .mpa(new Mpa(
                        3,
                        "PG-13")
                )
                .build());

        filmStorage.addFilm(Film.builder()
                .name("test3")
                .description("test3")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(new HashSet<>())
                .mpa(new Mpa(
                        3,
                        "PG-13")
                )
                .likesIds(Set.of(1, 2, 3))
                .build());
    }

    private void clearFilmDb() {
        films = filmStorage.getAllFilms();

        for (Film film : films) {
            filmStorage.deleteFilm(film.getId());
        }
    }

    @Test
    public void addFilmAndGetAllFilms() {
        films = filmStorage.getAllFilms();
        assertThat(films.get(0).getName()).isEqualTo("test1");
        assertThat(films.get(1).getDescription()).isEqualTo("test2");
    }

    @Test
    public void deleteFilm() {
        films = filmStorage.getAllFilms();
        assertThat(films.size()).isEqualTo(3);

        for (Film film : films) {
            filmStorage.deleteFilm(film.getId());
        }

        films = filmStorage.getAllFilms();
        assertThat(films.size()).isEqualTo(0);
    }

    @Test
    public void updateFilmAndGetFilmById() {
        filmStorage.addFilm(Film.builder()
                .name("test4")
                .description("test4")
                .releaseDate(LocalDate.now())
                .duration(100)
                .mpa(new Mpa(
                        3,
                        "PG-13")
                )
                .genres(new HashSet<>())
                .build());

        films = filmStorage.getAllFilms();
        Film film = filmStorage.getFilmById(films.get(0).getId()).get();
        film.setName("TestCheck");
        filmStorage.updateFilm(film);
        assertThat(filmStorage.getFilmById(films.get(0).getId()).get().getName()).isEqualTo("TestCheck");
    }

    @Test
    public void getMostPopularFilms() {
        assertThat(filmStorage.getMostPopularFilm(3).size()).isEqualTo(3);
        assertThat(filmStorage.getMostPopularFilm(1).get(0).getName()).isEqualTo("test3");
    }
}
