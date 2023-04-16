package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Primary
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sql = "INSERT INTO \"friend_list\" (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String sql = "DELETE FROM \"friend_list\" WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        List<User> friends = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT FRIEND_ID FROM \"friend_list\" WHERE USER_ID = ?", userId);

        while (userRows.next()) {
            Integer id = userRows.getInt("friend_id");
            friends.add(userStorage.getUserById(id).get());
        }

        return friends;
    }

    @Override
    public Set<User> getCommonFriendsIds(Integer userId, Integer otherUserId) {
        Set<User> friends = new HashSet<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT FRIEND_ID FROM \"friend_list\" " +
                        "WHERE USER_ID = ? AND FRIEND_ID IN (SELECT FRIEND_ID FROM \"friend_list\" WHERE USER_ID = ?)",
                userId,
                otherUserId);

        while (userRows.next()) {
            Integer id = userRows.getInt("friend_id");
            friends.add(userStorage.getUserById(id).get());
        }

        return friends;
    }
}
