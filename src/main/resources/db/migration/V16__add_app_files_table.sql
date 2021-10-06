-- unexpectedly table app_images was removed when I tried to rename it
-- so I create a completely new table app_files
SET SESSION foreign_key_checks=OFF;
ALTER TABLE courses DROP FOREIGN KEY IF EXISTS courses_ibfk_1;
ALTER TABLE authors DROP FOREIGN KEY IF EXISTS authors_ibfk_1;
ALTER TABLE comments DROP FOREIGN KEY IF EXISTS comments_ibfk_3;
DROP TABLE IF EXISTS app_files;
CREATE TABLE app_files (
    file_id INT AUTO_INCREMENT,
    file_path VARCHAR(2000) NOT NULL,
    PRIMARY KEY (file_id)
);
ALTER TABLE courses ADD FOREIGN KEY (image_id) REFERENCES app_files (file_id);
ALTER TABLE authors ADD FOREIGN KEY (image_id) REFERENCES app_files (file_id);
ALTER TABLE comments ADD FOREIGN KEY (image_id) REFERENCES app_files (file_id);
SET SESSION foreign_key_checks=ON;

