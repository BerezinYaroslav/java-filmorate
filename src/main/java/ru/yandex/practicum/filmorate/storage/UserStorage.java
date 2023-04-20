package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    User createUser(User user);

    Optional<User> updateUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Integer id);

    Optional<User> deleteUser(Integer userId);

    List<User> getAllFriends(Integer userId);

    Set<User> getCommonFriendsIds(Integer userId, Integer otherUserId);

    void deleteAllUsers();
}
