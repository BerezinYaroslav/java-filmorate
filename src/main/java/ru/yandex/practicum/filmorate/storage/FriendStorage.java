package ru.yandex.practicum.filmorate.storage;

public interface FriendStorage {
    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);
}
