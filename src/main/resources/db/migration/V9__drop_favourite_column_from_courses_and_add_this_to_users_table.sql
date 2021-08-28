ALTER TABLE courses DROP COLUMN favourite;

CREATE TABLE course_likes (
    course_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (course_id)
        REFERENCES courses(course_id),
    FOREIGN KEY (user_id)
        REFERENCES users(user_id),
    PRIMARY KEY (course_id, user_id)
)