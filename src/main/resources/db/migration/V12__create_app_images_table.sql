CREATE TABLE app_images (
    image_id INT AUTO_INCREMENT,
    image VARCHAR(2000) NOT NULL,
    PRIMARY KEY (image_id)
);

ALTER TABLE comments DROP COLUMN IF EXISTS image;
ALTER TABLE comments ADD COLUMN image_id INT NULL;
ALTER TABLE comments ADD FOREIGN KEY (image_id) REFERENCES app_images (image_id);