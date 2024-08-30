DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name     VARCHAR(250)                            NOT NULL,
    email    VARCHAR(254)                            NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories
(
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name        VARCHAR(50)                             NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS locations
(
    location_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    lat         DECIMAL                                 NOT NULL,
    lon         DECIMAL                                 NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    event_id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP                               NOT NULL,
    location_id        BIGINT                                  NOT NULL,
    created            TIMESTAMP                               NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  BIGINT                                  NOT NULL,
    request_moderation BOOLEAN                                 NOT NULL,
    state              VARCHAR(100)                            NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    CONSTRAINT events_location FOREIGN KEY (location_id) REFERENCES locations (location_id) ON DELETE CASCADE,
    CONSTRAINT category_names FOREIGN KEY (category_id) REFERENCES categories (category_id) ON DELETE CASCADE,
    CONSTRAINT initiator_constraint FOREIGN KEY (initiator_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    created      TIMESTAMP                               NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status       CHARACTER VARYING(100)                  NOT NULL,
    CONSTRAINT requests_events
        FOREIGN KEY (event_id) REFERENCES events (event_id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    pinned         BOOLEAN                                 NOT NULL,
    title          VARCHAR(50)                             NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT NOT NULL REFERENCES compilations (compilation_id) ON DELETE CASCADE,
    event_id       BIGINT NOT NULL REFERENCES events (event_id) ON DELETE CASCADE,
    PRIMARY KEY (compilation_id, event_id)
);
