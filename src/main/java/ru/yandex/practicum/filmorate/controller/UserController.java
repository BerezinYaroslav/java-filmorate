package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    private User createUser(@RequestBody User user) {
        validate(user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validate(user);
        return userService.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    private User deleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Integer userId,
                          @PathVariable Integer friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer userId,
                             @PathVariable Integer friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getAllFriends(@PathVariable Integer userId) {
        return userService.getAllFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public Set<User> getCommonFriends(@PathVariable Integer userId,
                                      @PathVariable Integer otherUserId) {
        return userService.getCommonFriendsIds(userId, otherUserId);
    }

    public static void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getEmail().isBlank()) {
            log.debug("Validation error: Email name can't be blank");
            throw new ValidationException("Email name can't be blank");
        }

        if (!user.getEmail().contains("@")) {
            log.debug("Validation error: Email name should contain '@'");
            throw new ValidationException("Email name should contain '@'");
        }

        if (user.getLogin().isBlank()) {
            log.debug("Validation error: Login can't be blank");
            throw new ValidationException("Login can't be blank");
        }

        if (user.getLogin().contains(" ")) {
            log.debug("Validation error: Login can't contain spaces");
            throw new ValidationException("Login can't contain spaces");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Validation error: Users birthday can't be in the future");
            throw new ValidationException("Users birthday can't be in the future");
        }
    }
}
