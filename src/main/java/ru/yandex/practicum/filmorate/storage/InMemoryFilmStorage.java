package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        return films.get(id);
    }

    @Override
    public Film createFilm(Film film) throws ValidationException {
        checkDescription(film);
        checkReleaseDate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) throws NotFoundException, ValidationException {
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        checkDescription(newFilm);
        checkReleaseDate(newFilm);
        Film oldFilm = films.get(newFilm.getId());
        if (oldFilm == null) {
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        return oldFilm;
    }

    private void checkDescription(Film film) throws ValidationException {
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Описание слишком длинное " + film.getDescription().length());
            throw new ValidationException("Описание не может быть более 200 символов");
        }
    }

    private void checkReleaseDate(Film film) throws ValidationException {
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.error("Дата релиза раньше даты возникновения кино " + film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть ранее 28 декабря 1895 года");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
