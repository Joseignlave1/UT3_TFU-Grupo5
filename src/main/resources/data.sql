-- Users
INSERT INTO users (username, email) VALUES ('alice', 'alice@example.com');
INSERT INTO users (username, email) VALUES ('bob', 'bob@example.com');

-- Projects
INSERT INTO projects (name, description) VALUES ('Project A', 'Sample project A');
INSERT INTO projects (name, description) VALUES ('Project B', 'Sample project B');

-- Tasks (need project_id from above)
INSERT INTO tasks (title, status, project_id) VALUES ('Task 1', 'OPEN', 1);
INSERT INTO tasks (title, status, project_id) VALUES ('Task 2', 'IN_PROGRESS', 1);
INSERT INTO tasks (title, status, project_id) VALUES ('Task 3', 'DONE', 2);

-- Assign users to tasks (many-to-many join table)
INSERT INTO task_users (task_id, user_id) VALUES (1, 1);
INSERT INTO task_users (task_id, user_id) VALUES (2, 2);
