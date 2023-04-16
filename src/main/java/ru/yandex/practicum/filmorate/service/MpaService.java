package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa getMpa(Integer mpaId) {
        log.debug("Mpa returned");

        if (mpaStorage.getMpa(mpaId) != null) {
            return mpaStorage.getMpa(mpaId);
        } else {
            throw new NotFoundException("Mpa with id " + mpaId + " not found");
        }
    }

    public List<Mpa> getAllMpa() {
        log.debug("All mpa returned");
        return mpaStorage.getAllMpa();
    }
}
