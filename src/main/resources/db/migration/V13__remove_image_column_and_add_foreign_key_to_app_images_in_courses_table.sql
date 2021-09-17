ALTER TABLE courses DROP COLUMN IF EXISTS image;
ALTER TABLE courses ADD COLUMN image_id INT NULL;
ALTER TABLE courses ADD FOREIGN KEY (image_id) REFERENCES app_images (image_id);