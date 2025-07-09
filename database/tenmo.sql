-- database tenmo
START TRANSACTION;

-- *************************************************************************************************
-- Drop all db tables
-- *************************************************************************************************
DROP TABLE IF EXISTS tenmo_user, tenmo_account, transfer, USD_account CASCADE;


CREATE TABLE tenmo_user (
	user_id INT GENERATED ALWAYS AS IDENTITY,
	username VARCHAR(50) NOT NULL UNIQUE,
	password_hash VARCHAR(200) NOT NULL,
	role VARCHAR(50) NOT NULL,
	CONSTRAINT pk_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username),
);

CREATE TABLE transfer (
    transfer_id INT GENERATED ALWAYS AS IDENTITY,
    sender_account_id int NOT NULL,
    recipient_account_id int NOT NULL,
    transfer_amount DEC NOT NULL,
    transfer_status VARCHAR(50) NOT NULL,
    transfer_type VARCHAR(50) NOT NULL,
    CONSTRAINT pk_transfer PRIMARY KEY (transfer_id),
    CONSTRAINT fk_sender FOREIGN KEY (sender_account_id) REFERENCES tenmo_account (tenmo_account_id),
    CONSTRAINT fk_recipient FOREIGN KEY (recipient_account_id) REFERENCES tenmo_account (tenmo_account_id)
    );


   CREATE TABLE tenmo_account (
   tenmo_account_id INT GENERATED ALWAYS AS IDENTITY,
   user_id int NOT NULL,
   te_bucks_balance dec NOT NULL,
   CONSTRAINT pk_tenmo_account PRIMARY KEY tenmo_account_id
   CONSTRAINT fk_user_id FOREIGN KEY user_id REFERENCES tenmo_user(user_id)
   );

    CREATE TABLE USD_account (
      USD_account_id INT GENERATED ALWAYS AS IDENTITY,
      tenmo_account_id int NOT NULL,
      usd_balance dec NOT NULL,
      user_id int NOT NULL,
      CONSTRAINT pk_USD_account PRIMARY KEY USD_account_id,
      CONSTRAINT fk_user_id FOREIGN KEY user_id REFERENCES tenmo_user(user_id)
      CONSTRAINT fk_tenmo_account_id FOREIGN KEY tenmo_account_id REFERENCES tenmo_account(tenmo_account_id)
      );







-- *************************************************************************************************
-- Create the tables and constraints
-- *************************************************************************************************
--INT GENERATED ALWAYS AS IDENTITY behaves similarly to SERIAL and creates an auto-incrementing
--integer value on inserting into the table

CREATE TABLE





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