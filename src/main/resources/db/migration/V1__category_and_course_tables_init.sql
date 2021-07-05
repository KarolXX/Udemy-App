CREATE TABLE categories (
    category_id INT AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    PRIMARY KEY (category_id)
);
CREATE TABLE courses (
    course_id INT AUTO_INCREMENT,
    title VARCHAR(40) NOT NULL,
    description VARCHAR(400),
    PRIMARY KEY (course_id)
);
CREATE TABLE course_category (
    category_id INT NOT NULL,
    course_id INT NOT NULL,
    FOREIGN KEY (category_id)
        REFERENCES categories(category_id)
        ON DELETE CASCADE,
    FOREIGN KEY (course_id)
        REFERENCES courses(course_id)
        ON DELETE CASCADE,
    PRIMARY KEY (category_id, course_id)
)