ALTER TABLE courses ADD COLUMN file_id VARCHAR(2000) NULL;
CREATE TABLE course_video (
    course_id INT NOT NULL,
    file_id INT NOT NULL,
    FOREIGN KEY (course_id)
       REFERENCES courses(course_id),
    FOREIGN KEY (file_id)
       REFERENCES app_files(file_id)
)