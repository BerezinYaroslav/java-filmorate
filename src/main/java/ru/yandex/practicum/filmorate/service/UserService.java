package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User createUser(User user) {
        log.debug("User '" + user.getName() + "' created successfully");
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        Optional<User> optionalUser = userStorage.updateUser(user);

        if (optionalUser.isEmpty()) {
            log.debug("Incorrect ID error: User with this ID does not exist when updating");
            throw new NotFoundException("User with this ID does not exist when updating");
        }

        log.debug("User '" + user.getName() + "' updated successfully");
        return optionalUser.get();
    }

    public List<User> getAllUsers() {
        log.debug("All users returned successfully");
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer userId) {
        Optional<User> optionalUser = userStorage.getUserById(userId);

        if (optionalUser.isEmpty()) {
            log.debug("Incorrect ID error: User with ID '" + userId + "' does not exist when getting by ID");
            throw new NotFoundException("User with ID '" + userId + "' does not exist when getting by ID");
        }

        log.debug("User with ID '" + userId + "' returned successfully");
        return optionalUser.get();
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user == null | friend == null) {
            log.debug("Incorrect ID error: Users do not exist when adding friend");
            throw new NotFoundException("Users do not exist when adding friend");
        }

        user.getFriendsIds().add(friendId);
        friend.getFriendsIds().add(userId);
        log.debug("Friend with ID '" + friendId + "' successfully added to user '" + userId + "'");
        log.debug("Friend with ID '" + userId + "' successfully added to user '" + friendId + "'");
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user == null | friend == null) {
            log.debug("Incorrect ID error: Users do not exist when deleting friend");
            throw new NotFoundException("Users do not exist when deleting friend");
        }
        if (!user.getFriendsIds().contains(friendId)) {
            log.debug("Incorrect ID error: User has not friend with id '" + friendId + "' when deleting friend");
            throw new NotFoundException("User has not friend with id '" + friendId + "' when deleting friend");
        }

        user.getFriendsIds().remove(friendId);
        friend.getFriendsIds().remove(userId);
        log.debug("Friend with ID '" + friendId + "' successfully removed from user '" + userId + "' friends list");
        log.debug("Friend with ID '" + userId + "' successfully removed from user '" + friendId + "' friends list");
    }

    public List<User> getAllFriends(Integer userId) {
        User user = getUserById(userId);
        List<User> users = new ArrayList<>();

        for (Integer friendsId : user.getFriendsIds()) {
            users.add(getUserById(friendsId));
        }

        log.debug("All friends returned successfully");
        return users;
    }

    public Set<User> getCommonFriendsIds(Integer userId, Integer otherUserId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);
        Set<User> commonFriends = new HashSet<>();

        for (Integer idFriend : user.getFriendsIds()) {
            for (Integer otherIdFriend : otherUser.getFriendsIds()) {
                if (Objects.equals(idFriend, otherIdFriend)) {
                    commonFriends.add(getUserById(idFriend));
                }
            }
        }

        log.debug("Common friends returned successfully");
        return commonFriends;
    }
}
