DROP TABLE IF EXISTS "user", "rating", "genre", "film", "film_genre", "like_list", "friend_list";

CREATE TABLE IF NOT EXISTS "user"
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    varchar NOT NULL,
    login    varchar NOT NULL UNIQUE,
    name     varchar NOT NULL,
    birthday date
);

CREATE TABLE IF NOT EXISTS "rating"
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS "genre"
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS "film"
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         varchar                          NOT NULL UNIQUE,
    description  varchar,
    rating_id    INTEGER REFERENCES "rating" (id) NOT NULL,
    release_date date,
    duration     INTEGER
);

CREATE TABLE IF NOT EXISTS "film_genre"
(
    film_id  INTEGER REFERENCES "film" (id)  NOT NULL,
    genre_id INTEGER REFERENCES "genre" (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS "like_list"
(
    film_id INTEGER REFERENCES "film" (id) NOT NULL,
    user_id INTEGER REFERENCES "user" (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS "friend_list"
(
    user_id   INTEGER REFERENCES "user" (id) NOT NULL,
    friend_id INTEGER REFERENCES "user" (id) NOT NULL
);