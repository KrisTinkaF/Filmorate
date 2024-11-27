package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film getFilmById(Long id);

    Film createFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws NotFoundException, ValidationException;
}
