BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user;

CREATE TABLE tenmo_user (
	user_id INT GENERATED ALWAYS AS IDENTITY,
	username VARCHAR(50) NOT NULL UNIQUE,
	password_hash VARCHAR(200) NOT NULL,
	role VARCHAR(50) NOT NULL,
	CONSTRAINT pk_user PRIMARY KEY (user_id)
);

INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user1','user1','ROLE_USER'); -- 1
INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user2','user2','ROLE_USER'); -- 2
INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user3','user3','ROLE_USER'); -- 3

COMMIT TRANSACTION;
