package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeStorageTest {
    private final LikeStorage likeStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @BeforeEach
    public void pullFilmAndUserDb() {
        filmStorage.addFilm(Film.builder()
                .name("film")
                .description("film")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(new HashSet<>())
                .mpa(new Mpa(
                        3,
                        "PG-13")
                )
                .build());

        userStorage.createUser(User.builder()
                .email("user@")
                .name("user")
                .login("user")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());
    }

    @Test
    public void likeAndUnlikeFilm() {
        Film film = filmStorage.getAllFilms().get(0);
        User user = userStorage.getAllUsers().get(0);

        likeStorage.likeFilm(
                film.getId(),
                user.getId());

        film = filmStorage.getAllFilms().get(0);
        Set<Integer> likes = film.getLikesIds();

        assertThat(likes.size()).isEqualTo(1);
        assertThat(likes).isEqualTo(Set.of(user.getId()));

        likeStorage.unlikeFilm(
                film.getId(),
                user.getId());

        film = filmStorage.getAllFilms().get(0);
        likes = film.getLikesIds();

        assertThat(likes.size()).isEqualTo(0);
    }
}
