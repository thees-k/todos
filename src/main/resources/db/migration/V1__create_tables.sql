-- src/main/resources/db/migration/V1__create_tables.sql

DROP SEQUENCE users_seq IF EXISTS;
DROP SEQUENCE todo_lists_seq IF EXISTS;
DROP SEQUENCE tasks_seq IF EXISTS;

DROP TABLE task_list_assignments IF EXISTS;
DROP TABLE todo_lists IF EXISTS;
DROP TABLE tasks IF EXISTS;
DROP TABLE users IF EXISTS;


CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE todo_lists_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE tasks_seq START WITH 1 INCREMENT BY 1;


CREATE TABLE users (
  id BIGINT DEFAULT NEXT VALUE FOR users_seq PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR NOT NULL UNIQUE,
  password_hash VARCHAR NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_by BIGINT,
  updated_at TIMESTAMP NOT NULL,
  CONSTRAINT fk_users_updatedby FOREIGN KEY (updated_by) REFERENCES users(id)
);

CREATE TABLE todo_lists (
  id BIGINT DEFAULT NEXT VALUE FOR todo_lists_seq PRIMARY KEY,
  user_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  is_public BOOLEAN DEFAULT FALSE,
  is_done BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  updated_by BIGINT,
  CONSTRAINT fk_todolist_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_todolist_updatedby FOREIGN KEY (updated_by) REFERENCES users(id)
);

CREATE TABLE tasks (
  id BIGINT DEFAULT NEXT VALUE FOR tasks_seq PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  is_done BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  updated_by BIGINT,
  CONSTRAINT fk_task_updatedby FOREIGN KEY (updated_by) REFERENCES users(id)
);

CREATE TABLE task_list_assignments (
  task_id BIGINT NOT NULL,
  list_id BIGINT NOT NULL,
  priority INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  updated_by BIGINT,
  PRIMARY KEY (task_id, list_id),
  CONSTRAINT fk_tla_task FOREIGN KEY (task_id) REFERENCES tasks(id),
  CONSTRAINT fk_tla_list FOREIGN KEY (list_id) REFERENCES todo_lists(id),
  CONSTRAINT fk_tla_updatedby FOREIGN KEY (updated_by) REFERENCES users(id)
);

