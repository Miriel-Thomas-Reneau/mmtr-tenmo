INSERT INTO tenmo_user (username, password_hash, role)
VALUES
	('user1', '$2a$10$hash', 'ROLE_USER'),
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
	(1, 2, 150.00, 'Approved', 'Sending'),
	(5, 3, 75.00, 'Pending', 'Request'),
	(4, 7, 100.00, 'Approved', 'Request'),
	(6, 3, 15.00, 'Pending', 'Sending'),
	(2, 1, 100.00, 'Rejected', 'Request'),
	(3, 1, 50.00, 'Approved', 'Sending'),
	(3, 1, 85.00, 'Pending', 'Request'),
	(1, 6, 100.00, 'Pending', 'Request');