UPDATE courses
SET users_number = (
    SELECT COALESCE( COUNT(cr.course_id), 0 ) FROM course_ratings AS cr
    WHERE cr.course_id = courses.course_id
);

UPDATE courses
SET sequence = (
    SELECT IF(c.average_rating > 4.4, POW(c.average_rating + 1, 2), POW(c.average_rating, 2)) * c.average_rating * c.users_number / IF(c.promotion IS NULL, IF(price = 0, 20, c.price), IF(c.promotion = 0, 5, c.promotion))
    FROM courses AS c
    WHERE c.course_id = courses.course_id
)