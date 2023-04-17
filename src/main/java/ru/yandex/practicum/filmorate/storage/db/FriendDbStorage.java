package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

@Component
@Primary
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String insertFriend = "INSERT INTO \"friend_list\" (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(insertFriend, userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String deleteFriend = "DELETE FROM \"friend_list\" WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(deleteFriend, userId, friendId);
    }
}
