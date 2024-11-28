package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) throws ValidationException {
        checkLogin(user);
        fillUserName(user);
        checkBirthday(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User newUser) throws NotFoundException, ValidationException {
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

    public void fillUserName(User user) {
        if (!StringUtils.hasText(user.getName())) {
            log.warn("Имя пользователя отсутствует");
            user.setName(user.getLogin());
        }
    }

    public void checkBirthday(User user) throws ValidationException {
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения в будущем " + user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public void checkLogin(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин содержит пробелы " + user.getLogin());
            throw new ValidationException("Логин не должен содержать пробелы");
        }
    }

    @Override
    public boolean existsById(Long userId) {
        if (users.get(userId) == null) {
            return false;
        }
        return true;
    }

    @Override
    public Collection<User> getFriends(Set<Long> friendsIds) {
        List<User> friends = new ArrayList<>();
        for (Long id : friendsIds) {
            friends.add(getUserById(id));
        }
        return friends;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
