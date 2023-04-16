package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {
    private final FilmService service;

    @GetMapping("/{mpaId}")
    public Mpa getMpa(@PathVariable Integer mpaId) {
        return service.getMpa(mpaId);
    }

    @GetMapping
    public List<Mpa> geAlltMpa() {
        return service.getAllMpa();
    }
}
