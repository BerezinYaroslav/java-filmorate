package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserValidationTest {
    @Test
    public void validationTest_equals_blankEmail() {
        User user = User.builder()
                .email("")
                .login("1")
                .name("1")
                .birthday(LocalDate.of(2003, 2, 14))
                .build();

        final ValidationException exception = assertThrows(ValidationException.class, () -> UserController.validate(user));
        assertEquals("Email name can't be blank", exception.getMessage());
    }

    @Test
    public void validationTest_equals_incorrectEmail() {
        User user = User.builder()
                .email("1") // without '@'
                .login("1")
                .name("1")
                .birthday(LocalDate.of(2003, 2, 14))
                .build();

        final ValidationException exception = assertThrows(ValidationException.class, () -> UserController.validate(user));
        assertEquals("Email name should contain '@'", exception.getMessage());
    }

    @Test
    public void validationTest_equals_blankLogin() {
        User user = User.builder()
                .email("1@")
                .login("")
                .name("1")
                .birthday(LocalDate.of(2003, 2, 14))
                .build();

        final ValidationException exception = assertThrows(ValidationException.class, () -> UserController.validate(user));
        assertEquals("Login can't be blank", exception.getMessage());
    }

    @Test
    public void validationTest_equals_incorrectLogin() {
        User user = User.builder()
                .email("1@")
                .login("1 1")
                .name("1")
                .birthday(LocalDate.of(2003, 2, 14))
                .build();

        final ValidationException exception = assertThrows(ValidationException.class, () -> UserController.validate(user));
        assertEquals("Login can't contain spaces", exception.getMessage());
    }

    @Test
    public void validationTest_equals_incorrectBirthday() {
        User user = User.builder()
                .email("1@")
                .login("11")
                .name("1")
                .birthday(LocalDate.of(2024, 2, 14))
                .build();

        final ValidationException exception = assertThrows(ValidationException.class, () -> UserController.validate(user));
        assertEquals("Users birthday can't be in the future", exception.getMessage());
    }

    @Test
    public void validationTest_equals_withoutName() {
        User user = User.builder()
                .email("1@")
                .login("1")
                .birthday(LocalDate.of(2003, 2, 14))
                .build();

        UserController.validate(user);
        assertEquals("1", user.getName());  // name = login when name == null
    }

    @Test
    public void validationTest_equals_blankName() {
        User user = User.builder()
                .email("1@")
                .login("1")
                .name("")
                .birthday(LocalDate.of(2003, 2, 14))
                .build();

        UserController.validate(user);
        assertEquals("1", user.getName());  // name = login when name == null
    }
}
