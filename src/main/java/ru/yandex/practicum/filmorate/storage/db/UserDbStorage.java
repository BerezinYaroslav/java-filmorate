package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        String insertUser = "INSERT INTO \"user\" (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertUser,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"user\" WHERE LOGIN = ?", user.getLogin());

        if (userRows.next()) {
            return new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate()
            );
        } else {
            return user;
        }
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (getUserById(user.getId()).isEmpty()) {
            return Optional.empty();
        }

        String updateUser = "UPDATE \"user\" SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE ID = ?";
        jdbcTemplate.update(updateUser,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        return Optional.of(user);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"user\"");

        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate()
            );

            for (Integer friendId : getFriends(user)) {
                user.getFriendsIds().add(friendId);
            }

            users.add(user);
        }

        return users;
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"user\" WHERE ID = ?", id);

        if (userRows.next()) {
            User user = new User(
                    id,
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate()
            );

            for (Integer friendId : getFriends(user)) {
                user.getFriendsIds().add(friendId);
            }

            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> deleteUser(Integer userId) {
        Optional<User> user = getUserById(userId);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        String deleteUser = "DELETE FROM \"user\" WHERE ID = ?";
        jdbcTemplate.update(deleteUser, userId);
        return user;
    }

    private Collection<Integer> getFriends(User user) {
        String selectFriend = "SELECT * FROM \"friend_list\" WHERE USER_ID = ?";
        return jdbcTemplate.query(selectFriend, (rs, rowNum) -> makeFriend(user, rs), user.getId());
    }

    private Integer makeFriend(User user, ResultSet rs) throws SQLException {
        return rs.getInt("friend_id");
    }
}
