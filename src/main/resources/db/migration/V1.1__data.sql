
INSERT INTO roles (id, name) VALUES (1, 'ALLOW_EDIT');

INSERT INTO users (id, username, password) VALUES (1, 'test', '$2a$10$W/LNB.vYSZT2TOsTpHQOKu7IBRYFEkjrY3ZwywudhlAAJDcQa/SFW');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
