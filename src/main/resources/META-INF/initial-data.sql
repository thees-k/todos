-- INSERT INTO todo (id, title, description, done) VALUES (1, 'Play with your cat', 'you both will like it', false);
-- INSERT INTO todo (id, title, description, done) VALUES (2, 'Read a book', 'you will learn something new', true);


DELETE FROM task_list_assignments;
DELETE FROM tasks;
DELETE FROM todo_lists;
DELETE FROM users;

INSERT INTO users (username, email, password_hash, updated_at) VALUES ('alice', 'alice@example.com', 'hash1', CURRENT_TIMESTAMP), ('bob', 'bob@example.com', 'hash2', CURRENT_TIMESTAMP), ('carol', 'carol@example.com', 'hash3', CURRENT_TIMESTAMP);

INSERT INTO todo_lists (user_id, name, description, is_public, is_done, created_at, updated_at, updated_by) VALUES (1, 'Alice’s Work Tasks', 'Work-related tasks', FALSE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1), (1, 'Alice’s Shared List', 'Shared list for project', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1), (2, 'Bob’s Private List', 'Personal stuff', FALSE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2), (3, 'Carol’s Open List', 'Anyone can help', TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3);

INSERT INTO tasks (title, description, is_done, created_at, updated_at, updated_by) VALUES ('Finish report', 'Prepare Q3 report for management', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1), ('Review code', 'Review PR #42', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1), ('Buy milk', 'Organic whole milk, 1L', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2), ('Fix bug', 'NPE in LoginService.java', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3), ('Plan team event', 'Ideas for team outing', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3);

INSERT INTO task_list_assignments (task_id, list_id, priority, created_at, updated_at, updated_by) VALUES (1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1), (2, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1), (1, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1), (5, 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3), (3, 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2), (4, 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3), (5, 4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3);

