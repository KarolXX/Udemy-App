CREATE TABLE author_course (
    author_id INT NOT NULL,
    course_id INT NOT NULL,
    FOREIGN KEY (author_id)
        REFERENCES authors (author_id),
    FOREIGN KEY (course_id)
        REFERENCES courses (course_id),
    PRIMARY KEY (author_id, course_id)
)