package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        checkLogin(user);
        fillUserName(user);
        checkBirthday(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        checkLogin(newUser);
        fillUserName(newUser);
        checkBirthday(newUser);
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        oldUser.setName(newUser.getName());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setBirthday(newUser.getBirthday());
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public void fillUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя отсутствует");
            user.setName(user.getLogin());
        }
    }

    public void checkBirthday(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения в будущем " + user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public void checkLogin(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Логин содержит пробелы " + user.getLogin());
            throw new ValidationException("Логин не должен содержать пробелы");
        }
    }
}
