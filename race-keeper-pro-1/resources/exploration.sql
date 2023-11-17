SELECT r.first_name, r.last_name, r.gender_code, race.race_name, rr.run_time
FROM runner r
JOIN runner_race rr ON r.runner_id = rr.runner_id
JOIN race ON rr.race_id = race.race_id
WHERE race.race_id = 3 AND rr.race_year = 2022 AND gender_code = 'F'
ORDER BY run_time ASC;  

SELECT AVG(rr.run_time)
FROM runner_race rr 
JOIN race ON rr.race_id = race.race_id
WHERE race.race_id = 3 AND rr.race_year = 2023;  