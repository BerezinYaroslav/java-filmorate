package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmValidationTest {
    private Film film;

    @Test
    public void validationTest_false_blankName() {
        film = Film.builder()
                .name("")
                .description("1")
                .releaseDate(LocalDate.of(2003, 2, 14))
                .duration(1)
                .build();

        final ValidationException exception = assertThrows(ValidationException.class, () -> FilmController.validate(film));
        assertEquals("Films name can't be blank", exception.getMessage());
    }

    @Test
    public void validationTest_false_longDescription() {
        film = Film.builder()
                .name("1")
                .description("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111")
                .releaseDate(LocalDate.of(2003, 2, 14))
                .duration(1)
                .build();

        final ValidationException exception = assertThrows(ValidationException.class, () -> FilmController.validate(film));
        assertEquals("Films description can't be longer than 200 chars", exception.getMessage());
    }

    @Test
    public void validationTest_false_incorrectDate() {
        film = Film.builder()
                .name("1")
                .description("1")
                .releaseDate(LocalDate.of(1054, 2, 20))
                .duration(1)
                .build();

        final ValidationException exception = assertThrows(ValidationException.class, () -> FilmController.validate(film));
        assertEquals("Films release date can't be before 28.12.1895", exception.getMessage());
    }

    @Test
    public void validationTest_false_incorrectDuration() {
        film = Film.builder()
                .name("1")
                .description("1")
                .releaseDate(LocalDate.of(2003, 2, 14))
                .duration(-1)
                .build();

        final ValidationException exception = assertThrows(ValidationException.class, () -> FilmController.validate(film));
        assertEquals("Films duration can't be negative", exception.getMessage());
    }
}
