DELETE FROM task_list_assignments;
DELETE FROM tasks;
DELETE FROM todo_lists;
DELETE FROM users;

-- NOTE:
-- password for user 'admin' is 'admin'
-- password for user 'alice' is 'alice'
-- password for user 'bob' is 'bob'
-- password for user 'carol' is 'carol'
INSERT INTO users (id, username, email, password_hash, created_at, updated_by, updated_at) VALUES (1, 'admin', 'admin@example.com', '$2a$10$zydPzLrV0pN7UV90nzOIje/xWsvbSjHDD6xFPbig9SDVD6FKEj3gO', CURRENT_TIMESTAMP, null, CURRENT_TIMESTAMP), (2, 'alice', 'alice@example.com', '$2a$10$lbhNMm/AJ2bDztbctzNtWu.FN6GV9Ejjl28Ab.7ylw9wJqsB/50lC', CURRENT_TIMESTAMP, null, CURRENT_TIMESTAMP), (3, 'bob', 'bob@example.com', '$2a$10$hq/YohNSNou/20lUNI1VAO7qr.nzldvMOzG15GIYZjQy8D0B.u.qC', CURRENT_TIMESTAMP, null, CURRENT_TIMESTAMP), (4, 'carol', 'carol@example.com', '$2a$10$IazaHCbTQOg3SjUnTD0DueBxXO4H7801c96I0BKTmcK2iwngTPP6y', CURRENT_TIMESTAMP, null, CURRENT_TIMESTAMP);

UPDATE users SET updated_by = id;

INSERT INTO todo_lists (id, user_id, name, description, is_public, is_done, created_at, updated_at, updated_by) VALUES (1, 2, 'Alice’s Work Tasks', 'Work-related tasks', FALSE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2), (2, 2, 'Alice’s Shared List', 'Shared list for project', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2), (3, 3, 'Bob’s Private List', 'Personal stuff', FALSE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3), (4, 4, 'Carol’s Open List', 'Anyone can help', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4);

INSERT INTO tasks (id, title, description, is_done, created_at, updated_at, updated_by) VALUES (1, 'Finish report', 'Prepare Q3 report for management', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2), (2, 'Review code', 'Review PR #42', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2), (3, 'Buy milk', 'Organic whole milk, 1L', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3), (4, 'Fix bug', 'NPE in LoginService.java', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4), (5, 'Plan team event', 'Ideas for team outing', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4);

INSERT INTO task_list_assignments (task_id, list_id, priority, created_at, updated_at, updated_by) VALUES (1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2), (2, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2), (1, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2), (5, 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4), (3, 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3), (4, 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4), (5, 4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4);