package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {
    private final UserStorage userStorage;
    private List<User> users;

    @Test
    public void createUserAndGetAllUsers() {
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
        userStorage.createUser(User.builder()
                .email("test4@")
                .name("test4")
                .login("test4")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test5@")
                .name("test5")
                .login("test5")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test6@")
                .name("test6")
                .login("test6")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        users = userStorage.getAllUsers();
        assertThat(users.size()).isEqualTo(10);

        for (User user : users) {
            userStorage.deleteUser(user.getId());
        }

        users = userStorage.getAllUsers();
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    public void updateUserAndGetUserById() {
        userStorage.createUser(User.builder()
                .email("test7@")
                .name("test7")
                .login("test7")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test8@")
                .name("test8")
                .login("test8")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test9@")
                .name("test9")
                .login("test9")
                .birthday(LocalDate.of(
                        1990,
                        1,
                        1))
                .build());

        userStorage.createUser(User.builder()
                .email("test10@")
                .name("test10")
                .login("test10")
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
