package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa getMpa(Integer mpaId) {
        Optional<Mpa> mpa = mpaStorage.getMpa(mpaId);

        if (mpa.isEmpty()) {
            throw new NotFoundException("Mpa with id " + mpaId + " not found");
        }

        log.debug("Mpa with id'" + mpaId + "' returned successfully");
        return mpa.get();
    }

    public List<Mpa> getAllMpa() {
        log.debug("All mpa returned");
        return mpaStorage.getAllMpa();
    }
}
