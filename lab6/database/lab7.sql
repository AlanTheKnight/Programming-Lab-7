-- Drop types
DROP DOMAIN IF EXISTS y_coordinate CASCADE;
DROP TYPE IF EXISTS coordinate_t CASCADE;
DROP TYPE IF EXISTS hair_color_t CASCADE;
DROP TYPE IF EXISTS nationality_t CASCADE;
DROP TYPE IF EXISTS position_t CASCADE;
DROP TYPE IF EXISTS status_t CASCADE;

-- Drop tables
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS workers CASCADE;
DROP TABLE IF EXISTS persons CASCADE;

-- Create types
CREATE DOMAIN y_coordinate as float check ( value > -154);
CREATE TYPE position_t AS ENUM ('HEAD_OF_DEPARTMENT', 'DEVELOPER', 'COOK', 'CLEANER');
CREATE TYPE status_t AS ENUM ('RECOMMENDED_FOR_PROMOTION', 'HIRED', 'PROBATION', 'REGULAR', 'FIRED');
CREATE TYPE nationality_t AS ENUM ('UNITED_KINGDOM', 'SOUTH_KOREA', 'NORTH_KOREA');
CREATE TYPE hair_color_t AS ENUM ('GREEN', 'RED', 'WHITE');

CREATE TYPE coordinate_t AS
(
    y y_coordinate,
    x integer
);

CREATE TABLE users
(
    id            serial PRIMARY KEY,
    username      varchar(20) NOT NULL CHECK (username ~ '^[a-zA-Z0-9_]{4,20}$'),
    -- 16-byte password salt
    password_salt char(32)    NOT NULL,
    -- SHA-512 password hash
    password_hash char(128)   NOT NULL
);


CREATE TABLE persons
(
    id          serial PRIMARY KEY,
    weight      bigint NOT NULL CHECK (weight > 0),
    height      float  NOT NULL CHECK (height > 0),
    nationality nationality_t DEFAULT NULL,
    hair_color  hair_color_t  DEFAULT NULL
);

CREATE TABLE workers
(
    id          serial PRIMARY KEY,
    name        varchar(50)  NOT NULL,
    owner       integer               DEFAULT NULL REFERENCES users (id) ON DELETE SET NULL,
    coordinates coordinate_t NOT NULL,
    salary      bigint       NOT NULL CHECK (salary > 0),
    end_date    date         NOT NULL,
    position    position_t            DEFAULT NULL,
    status      status_t     NOT NULL DEFAULT 'HIRED',
    person_id   integer      NOT NULL REFERENCES persons (id) ON DELETE CASCADE,
    created_at  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
