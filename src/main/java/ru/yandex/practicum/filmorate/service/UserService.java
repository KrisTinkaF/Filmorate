package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> addAsFriend(Long userId, Long friendId) throws NotFoundException {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        Set<Long> friends = user.getFriends();
        friends.add(friendId);
        user.setFriends(friends);
        return getFriends(friends);
    }

    public Collection<User> removeFromFriends(Long userId, Long friendId) throws NotFoundException {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        Set<Long> friends = user.getFriends();
        friends.remove(friendId);
        user.setFriends(friends);
        return getFriends(friends);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) throws NotFoundException {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (userStorage.getUserById(otherId) == null) {
            throw new NotFoundException("Пользователь с id = " + otherId + " не найден");
        }
        Set<Long> userFriends = userStorage.getUserById(userId).getFriends();
        Set<Long> otherUserFriends = userStorage.getUserById(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();
        userFriends.retainAll(otherUserFriends);
        for (Long id : userFriends) {
            commonFriends.add(userStorage.getUserById(id));
        }
        return commonFriends;
    }

    public Collection<User> getUserFriends(Long userId) throws NotFoundException {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Set<Long> friends = user.getFriends();
        return getFriends(friends);
    }

    private Collection<User> getFriends(Set<Long> friendsIds) {
        List<User> friends = new ArrayList<>();
        for (Long id : friendsIds) {
            friends.add(userStorage.getUserById(id));
        }
        return friends;
    }
}
