CREATE TYPE coordinate AS (
	y float
	x int
);
CREATE TABLE "users" (
	"id" serial NOT NULL,
	"username" varchar(20) NOT NULL CHECK(username ~ '^[a-zA-Z0-9_]{4,20}$'),
	-- 16-byte password salt
	"password_salt" char(32) NOT NULL,
	-- SHA-256 password hash
	"password_hash" char(64) NOT NULL,
	PRIMARY KEY("id")
);

CREATE TYPE "position_t" AS ENUM ('HEAD_OF_DEPARTMENT', 'DEVELOPER', '    COOK', '    CLEANER');

,CREATE TYPE "status_t" AS ENUM ('    RECOMMENDED_FOR_PROMOTION', 'HIRED', 'PROBATION', 'REGULAR', 'FIRED');

CREATE TABLE "workers" (
	"id" serial NOT NULL,
	"owner" int DEFAULT NULL,
	"coordinates" coordinate NOT NULL,
	"salary" bigint NOT NULL CHECK(salary > 0),
	"end_date" date NOT NULL,
	"position" position_t DEFAULT NULL,
	"status" status_t NOT NULL DEFAULT 'HIRED',
	"person_id" int NOT NULL,
	"created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY("id")
);

CREATE TYPE "nationality_t" AS ENUM ('UNITED_KINGDOM', 'SOUTH_KOREA', 'NORTH_KOREA');

,CREATE TYPE "hair_color_t" AS ENUM ('GREEN', 'RED', 'WHITE');

CREATE TABLE "persons" (
	"id" serial NOT NULL,
	"weight" bigint NOT NULL CHECK(weight > 0),
	"height" double NOT NULL CHECK(height > 0),
	"nationality" nationality_t DEFAULT NULL,
	"hair_color" hair_color_t DEFAULT NULL,
	PRIMARY KEY("id")
);

ALTER TABLE "workers"
ADD FOREIGN KEY("owner") REFERENCES "users"("id")
ON UPDATE NO ACTION ON DELETE SET NULL;
ALTER TABLE "workers"
ADD FOREIGN KEY("person_id") REFERENCES "persons"("id")
ON UPDATE NO ACTION ON DELETE CASCADE;