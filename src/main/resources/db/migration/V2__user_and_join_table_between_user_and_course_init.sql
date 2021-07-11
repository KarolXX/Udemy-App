

CREATE TABLE users (
    user_id INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (user_id)
);
CREATE TABLE course_ratings (
    user_id INT NOT NULL,
    course_id INT NOT NULL,
    rating INT,
    FOREIGN KEY (user_id)
        REFERENCES users(user_id),
    FOREIGN KEY (course_id)
        REFERENCES courses(course_id),
    PRIMARY KEY (user_id, course_id)
)