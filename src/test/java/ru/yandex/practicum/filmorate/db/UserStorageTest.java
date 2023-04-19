package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {
    private final UserStorage userStorage;
    private List<User> users;

    private void clearUserDb() {
        users = userStorage.getAllUsers();

        for (User user : users) {
            userStorage.deleteUser(user.getId());
        }
    }

    @Test
    public void createUserAndGetAllUsers() {
        clearUserDb();

        userStorage.createUser(User.builder()
                .email("test1@")
                .name("test1")
                .login("test1")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test2@")
                .name("test2")
                .login("test2")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test3@")
                .name("test3")
                .login("test3")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        users = userStorage.getAllUsers();
        assertThat(users.get(0).getName()).isEqualTo("test1");
        assertThat(users.get(1).getEmail()).isEqualTo("test2@");
    }

    @Test
    public void deleteUser() {
        clearUserDb();

        userStorage.createUser(User.builder()
                .email("test1@")
                .name("test1")
                .login("test1")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test2@")
                .name("test2")
                .login("test2")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test3@")
                .name("test3")
                .login("test3")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        users = userStorage.getAllUsers();
        assertThat(users.size()).isEqualTo(3);

        for (User user : users) {
            userStorage.deleteUser(user.getId());
        }

        users = userStorage.getAllUsers();
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    public void updateUserAndGetUserById() {
        clearUserDb();

        userStorage.createUser(User.builder()
                .email("test1@")
                .name("test1")
                .login("test1")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test2@")
                .name("test2")
                .login("test2")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test3@")
                .name("test3")
                .login("test3")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test4@")
                .name("test4")
                .login("test4")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        users = userStorage.getAllUsers();
        User user = userStorage.getUserById(users.get(0).getId()).get();
        user.setName("TestCheck");
        userStorage.updateUser(user);
        assertThat(userStorage.getUserById(users.get(0).getId()).get().getName()).isEqualTo("TestCheck");
    }
}
