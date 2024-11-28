package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Set;


@Service
public class FilmService {
    private final FilmStorage filmStorage;

    private final UserService userService;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film setLike(Long filmId, Long userId) throws NotFoundException {
        Film film = getFilm(filmId);
        User user = userService.getUser(userId);
        Set<Long> likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) throws NotFoundException {
        Film film = getFilm(filmId);
        User user = userService.getUser(userId);
        Set<Long> likes = film.getLikes();
        likes.remove(userId);
        film.setLikes(likes);
        return film;
    }

    public Collection<Film> getMostPopular(int count) throws ValidationException {
        if (count <= 0) {
            throw new ValidationException("count должно быть больше нуля");
        }
        Collection<Film> sortedFilms = filmStorage.sortByLikes();
        return sortedFilms.stream().limit(count).toList();
    }

    public Film getFilm(Long filmId) throws NotFoundException {
        if (!filmStorage.existsById(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        return filmStorage.getFilmById(filmId);
    }
}
