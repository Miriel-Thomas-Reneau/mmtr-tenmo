BEGIN TRANSACTION;

DROP TABLE IF EXISTS transfer, USD_account, tenmo_account, tenmo_user CASCADE;

CREATE TABLE tenmo_user (
	user_id INT GENERATED ALWAYS AS IDENTITY,
	username VARCHAR(50) NOT NULL UNIQUE,
	password_hash VARCHAR(200) NOT NULL,
	role VARCHAR(50) NOT NULL,
	CONSTRAINT pk_user PRIMARY KEY (user_id),
	CONSTRAINT uq_username UNIQUE (username)
);

CREATE TABLE tenmo_account (
	tenmo_account_id INT GENERATED ALWAYS AS IDENTITY,
	user_id INT NOT NULL,
	te_bucks_balance DECIMAL NOT NULL,
	CONSTRAINT pk_tenmo_account PRIMARY KEY (tenmo_account_id),
	CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES tenmo_user(user_id),
	CONSTRAINT uq_user_id UNIQUE (user_id)
);

CREATE TABLE USD_account (
	USD_account_id INT GENERATED ALWAYS AS IDENTITY,
	tenmo_account_id INT NOT NULL,
	usd_balance DECIMAL NOT NULL DEFAULT 0.00,
	user_id INT NOT NULL,
	CONSTRAINT pk_USD_account PRIMARY KEY (USD_account_id),
	CONSTRAINT fk_usd_user_id FOREIGN KEY (user_id) REFERENCES tenmo_user(user_id),
	CONSTRAINT fk_tenmo_account_id FOREIGN KEY (tenmo_account_id) REFERENCES tenmo_account(tenmo_account_id),
	CONSTRAINT uq_usd_user_id UNIQUE (user_id)
);

CREATE TABLE transfer (
	transfer_id INT GENERATED ALWAYS AS IDENTITY,
	sender_account_id INT NOT NULL,
	recipient_account_id INT NOT NULL,
	transfer_amount DECIMAL NOT NULL,
	transfer_status VARCHAR(50) NOT NULL,
	transfer_type VARCHAR(50) NOT NULL,
	CONSTRAINT pk_transfer PRIMARY KEY (transfer_id),
	CONSTRAINT fk_sender FOREIGN KEY (sender_account_id) REFERENCES tenmo_account(tenmo_account_id),
	CONSTRAINT fk_recipient FOREIGN KEY (recipient_account_id) REFERENCES tenmo_account(tenmo_account_id),
	CONSTRAINT chk_sender_recipient_not_equal CHECK (recipient_account_id != sender_account_id),
	CONSTRAINT chk_amount_positive CHECK (transfer_amount > 0),
	CONSTRAINT chk_transfer_status CHECK (transfer_status IN ('Approved', 'Pending', 'Rejected')),
	CONSTRAINT chk_transfer_type CHECK (transfer_type IN ('Sending', 'Request'))
);

--CREATE OR REPLACE FUNCTION block_admin_accounts()
--RETURNS TRIGGER AS $$
--BEGIN
--	IF EXISTS (
--		SELECT 1 FROM tenmo_user WHERE user_id = NEW.user_id AND role = 'ROLE_ADMIN'
--	) THEN
--		RAISE EXCEPTION 'Admins are not allowed to have Tenmo accounts.';
--	END IF;
--	RETURN NEW;
--END;
--$$ LANGUAGE plpgsql;
--
--CREATE TRIGGER trg_no_admin_accounts
--BEFORE INSERT ON tenmo_account
--FOR EACH ROW
--EXECUTE FUNCTION block_admin_accounts();

INSERT INTO tenmo_user (username, password_hash, role)
VALUES
	('user1', 'user1', 'ROLE_USER'),
	('user2', '$2a$10$hash', 'ROLE_USER'),
	('user3', '$2a$10$hash', 'ROLE_USER'),
	('user4', '$2a$10$hash', 'ROLE_USER'),
	('user5', '$2a$10$hash', 'ROLE_USER'),
	('admin1', '$2a$10$hash', 'ROLE_ADMIN'),
	('admin2', '$2a$10$hash', 'ROLE_ADMIN'),
	('johndoe', '$2a$10$hash', 'ROLE_USER'),
	('janedoe', '$2a$10$hash', 'ROLE_USER'),
	('superadmin', '$2a$10$hash', 'ROLE_ADMIN');

INSERT INTO tenmo_account (user_id, te_bucks_balance)
VALUES
	(1, 1000.00),
	(2, 1000.00),
	(3, 1000.00),
	(4, 1000.00),
	(5, 1000.00),
	(8, 1000.00),
	(9, 1000.00);

INSERT INTO USD_account (tenmo_account_id, user_id)
VALUES
	(1, 1),
	(2, 2),
	(3, 3),
	(4, 4),
	(5, 5),
	(6, 8),
	(7, 9);

INSERT INTO transfer (sender_account_id, recipient_account_id, transfer_amount, transfer_status, transfer_type)
VALUES
	(1, 2, 150.01, 'Approved', 'Sending'),
	(5, 3, 75.01, 'Pending', 'Request'),
	(4, 7, 100.01, 'Approved', 'Request'),
	(6, 3, 15.01, 'Pending', 'Sending'),
	(2, 1, 100.01, 'Rejected', 'Request'),
	(3, 1, 50.01, 'Approved', 'Sending'),
	(3, 1, 85.01, 'Pending', 'Request'),
	(1, 6, 100.01, 'Pending', 'Request');

COMMIT TRANSACTION;