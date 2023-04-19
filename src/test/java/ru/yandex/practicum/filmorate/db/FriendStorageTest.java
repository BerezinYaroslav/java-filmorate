package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendStorageTest {
    private final FriendStorage friendStorage;
    private final UserStorage userStorage;
    private List<User> users;

    private void clearUserDb() {
        users = userStorage.getAllUsers();

        for (User user : users) {
            userStorage.deleteUser(user.getId());
        }
    }

    @Test
    public void addAndDeleteFriend() {
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

        users = userStorage.getAllUsers();
        friendStorage.addFriend(
                users.get(0).getId(),
                users.get(1).getId());

        users = userStorage.getAllUsers();
        assertThat(users.get(0).getFriendsIds().size()).isEqualTo(1);
        assertThat(users.get(0).getFriendsIds()).isEqualTo(Set.of(users.get(1).getId()));

        friendStorage.deleteFriend(
                users.get(0).getId(),
                users.get(1).getId());

        users = userStorage.getAllUsers();
        assertThat(users.get(0).getFriendsIds().size()).isEqualTo(0);
    }

    @Test
    public void getAllFriends() {
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

        users = userStorage.getAllUsers();
        friendStorage.addFriend(
                users.get(0).getId(),
                users.get(1).getId());

        users = userStorage.getAllUsers();
        assertThat(users.get(0).getFriendsIds().size()).isEqualTo(userStorage.getAllFriends(users.get(0).getId()).size());
    }
}
