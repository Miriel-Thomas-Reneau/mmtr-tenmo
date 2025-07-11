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
	te_bucks_balance DECIMAL(19,2) NOT NULL,
	CONSTRAINT pk_tenmo_account PRIMARY KEY (tenmo_account_id),
	CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES tenmo_user(user_id),
	CONSTRAINT uq_user_id UNIQUE (user_id)
);

CREATE TABLE USD_account (
	USD_account_id INT GENERATED ALWAYS AS IDENTITY,
	tenmo_account_id INT NOT NULL,
	usd_balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,
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
	transfer_amount DECIMAL(19,2) NOT NULL,
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