package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    Collection<User> getAllUsers();

    User getUserById(Long id);

    User createUser(User user) throws ValidationException;

    User updateUser(User user) throws NotFoundException, ValidationException;

    boolean existsById(Long userId);

    Collection<User> getFriends(Set<Long> friendsIds);
}
