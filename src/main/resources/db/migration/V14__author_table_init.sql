CREATE TABLE authors (
    author_id INT AUTO_INCREMENT,
    name VARCHAR(70),
    occupation VARCHAR(200),
    description VARCHAR(5000),
    image_id INT,
    FOREIGN KEY (image_id)
        REFERENCES app_images (image_id),
    PRIMARY KEY(author_id)
)