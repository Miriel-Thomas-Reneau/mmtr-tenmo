-- database tenmo
START TRANSACTION;

-- *************************************************************************************************
-- Drop all db tables
-- *************************************************************************************************
DROP TABLE IF EXISTS tenmo_user;

-- *************************************************************************************************
-- Create the tables and constraints
-- *************************************************************************************************
--INT GENERATED ALWAYS AS IDENTITY behaves similarly to SERIAL and creates an auto-incrementing
--integer value on inserting into the table
CREATE TABLE tenmo_user (
	user_id INT GENERATED ALWAYS AS IDENTITY,
	username VARCHAR(50) NOT NULL UNIQUE,
	password_hash VARCHAR(200) NOT NULL,
	role VARCHAR(50) NOT NULL,
	CONSTRAINT pk_user PRIMARY KEY (user_id)
);

-- *************************************************************************************************
-- Insert some sample starting data
-- *************************************************************************************************
-- Password for all users is password
INSERT INTO
	tenmo_user (username, password_hash, role)
VALUES
	('user','$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_USER'),
	('admin','$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_ADMIN');

COMMIT;