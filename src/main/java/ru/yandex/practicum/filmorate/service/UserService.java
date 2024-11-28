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
        User user = getUser(userId);
        User friend = getUser(friendId);
        Set<Long> friends = user.getFriends();
        friends.add(friendId);
        user.setFriends(friends);
        return userStorage.getFriends(friends);
    }

    public Collection<User> removeFromFriends(Long userId, Long friendId) throws NotFoundException {
        User user = getUser(userId);
        User friend = getUser(friendId);
        Set<Long> friends = user.getFriends();
        friends.remove(friendId);
        user.setFriends(friends);
        return userStorage.getFriends(friends);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) throws NotFoundException {
        Set<Long> userFriends = getUser(userId).getFriends();
        Set<Long> otherUserFriends = getUser(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();
        userFriends.retainAll(otherUserFriends);
        for (Long id : userFriends) {
            commonFriends.add(userStorage.getUserById(id));
        }
        return commonFriends;
    }

    public Collection<User> getUserFriends(Long userId) throws NotFoundException {
        User user = getUser(userId);
        Set<Long> friends = user.getFriends();
        return userStorage.getFriends(friends);
    }

    public User getUser(Long userId) throws NotFoundException {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        return userStorage.getUserById(userId);
    }
}
