UPDATE courses
SET average_rating = (
    SELECT COALESCE( AVG(cr.rating), 0 ) FROM course_ratings AS cr
    WHERE cr.course_id = courses.course_id
)