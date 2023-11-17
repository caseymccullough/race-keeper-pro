START TRANSACTION;

DROP TABLE IF EXISTS race, runner, runner_race CASCADE;

CREATE TABLE race (
    race_id serial NOT NULL,
    race_name character varying(100) NOT NULL,
    race_city character varying(100) NOT NULL,
    race_state_code character(2) NOT NULL,
    race_distance double precision
);

CREATE TABLE runner (
    runner_id serial NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    street character varying(100),
    city character varying(100),
    state_code character(2),
    gender_code character(1),
    birthday date NOT NULL
);


CREATE TABLE runner_race (
    runner_id integer NOT NULL,
    race_id integer NOT NULL,
    run_time interval,
    race_year integer DEFAULT '-1'::integer NOT NULL
);

ALTER TABLE ONLY runner
    ADD CONSTRAINT pk_customer PRIMARY KEY (runner_id);

ALTER TABLE ONLY race
    ADD CONSTRAINT race_pkey PRIMARY KEY (race_id);

ALTER TABLE ONLY runner_race
    ADD CONSTRAINT fk_runner_race_race FOREIGN KEY (race_id) REFERENCES race(race_id);

ALTER TABLE ONLY runner_race
    ADD CONSTRAINT fk_runner_race_runner FOREIGN KEY (runner_id) REFERENCES runner(runner_id);


-- 2 Races 
INSERT INTO race (race_name, race_city, race_state_code, race_distance) VALUES
('Boston Marathon', 'Boston', 'MA', 26.2);
INSERT INTO race (race_name, race_city, race_state_code, race_distance) VALUES
('California International Marathon', 'Sacramento',	'CA', 26.2);

-- 6 Runners 3 M / 3 F
INSERT INTO runner (first_name, last_name, street, city, state_code, gender_code, birthday) VALUES
('Male1', 'Runner', '40 Main Street', 'Boulder', 'CO', 'M',	'1971-03-09');
INSERT INTO runner (first_name, last_name, street, city, state_code, gender_code, birthday) VALUES
('Male2',	'Runner',	'22 Sunshine Lane',	'Phoenix', 'AZ', 'M', '2000-01-01');
INSERT INTO runner (first_name, last_name, street, city, state_code, gender_code, birthday) VALUES
('Male3', 'Runner', '100 Lazywood Road', 'New York', 'NY', 'M', '1990-02-02');

INSERT INTO runner (first_name, last_name, street, city, state_code, gender_code, birthday) VALUES
('Female1', 'Runner', '40 Main Street', 'Boulder', 'CO', 'F',	'1971-03-09');
INSERT INTO runner (first_name, last_name, street, city, state_code, gender_code, birthday) VALUES
('Female2',	'Runner',	'22 Sunshine Lane',	'Phoenix', 'AZ', 'F', '2000-01-01');
INSERT INTO runner (first_name, last_name, street, city, state_code, gender_code, birthday) VALUES
('Female3', 'Runner', '100 Lazywood Road', 'New York', 'NY', 'F', '1990-02-02');

-- Male1 beats Male2 beats Male3 in race 1
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (1, 1,'03:00:00', 2023);
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (2, 1,'04:00:00', 2023);
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (3, 1,'05:00:00', 2023);

-- Male3 beats Male2 beats Male1 in race 2
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (1, 2,'05:30:00', 2023);
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (2, 2,'04:30:00', 2023);
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (3, 2,'03:30:00', 2023);

-- Female1 beats Female2 beats Female3 in race 1
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (4, 1,'03:00:00', 2023);
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (5, 1,'04:00:00', 2023);
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (6, 1,'05:00:00', 2023);

-- Female3 beats Female2 beats Female1 in race 2
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (4, 2,'05:30:00', 2023);
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (5, 2,'04:30:00', 2023);
INSERT INTO runner_race (runner_id, race_id, run_time, race_year) VALUES (6, 2,'03:30:00', 2023);

COMMIT; 


