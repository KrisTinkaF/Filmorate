package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;


@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film setLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Set<Long> likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Set<Long> likes = film.getLikes();
        likes.remove(userId);
        film.setLikes(likes);
        return film;
    }

    public Collection<Film> getMostPopular(int count) throws ValidationException {
        if (count <= 0) {
            throw new ValidationException("count должно быть больше нуля");
        }
        Collection<Film> sortedFilms = filmStorage.getAllFilms().stream().sorted(Comparator.comparing(film -> film.getLikes().size())).toList();
        return sortedFilms.stream().limit(count).toList();
    }
}
