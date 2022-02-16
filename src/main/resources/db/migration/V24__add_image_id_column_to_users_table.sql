ALTER TABLE users ADD COLUMN image_id INT NULL;
ALTER TABLE users ADD FOREIGN KEY (image_id) REFERENCES app_files(file_id);