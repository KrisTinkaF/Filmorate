package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserStorage userStorage;

    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userStorage.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userStorage.getUserById(userId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getUserFriends(@PathVariable Long userId) throws NotFoundException {
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long userId,
                                           @PathVariable Long otherId) throws NotFoundException {
        return userService.getCommonFriends(userId, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) throws NotFoundException, ValidationException {
        return userStorage.updateUser(newUser);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public Collection<User> addAsFriend(@PathVariable Long userId,
                       @PathVariable Long friendId) throws NotFoundException {
        return userService.addAsFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public Collection<User> deleteFromFriends(@PathVariable Long userId,
                                        @PathVariable Long friendId) throws NotFoundException {
        return userService.removeFromFriends(userId, friendId);
    }


}
