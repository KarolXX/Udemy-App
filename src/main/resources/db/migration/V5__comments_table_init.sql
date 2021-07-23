CREATE TABLE comments (
    comment_id INT AUTO_INCREMENT,
    text VARCHAR(500) NOT NULL,
    user_id INT,
    course_id INT,
    FOREIGN KEY (user_id)
        REFERENCES users(user_id),
    FOREIGN KEY (course_id)
        REFERENCES courses(course_id),
    PRIMARY KEY (comment_id)
)