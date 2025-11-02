-- === Hace la idempotencia posible (claves únicas) ===
CREATE UNIQUE INDEX IF NOT EXISTS users_email_key    ON users(email);
CREATE UNIQUE INDEX IF NOT EXISTS projects_name_key  ON projects(name);
CREATE UNIQUE INDEX IF NOT EXISTS tasks_title_proj_uq ON tasks(title, project_id);
CREATE UNIQUE INDEX IF NOT EXISTS task_users_task_user_uq ON task_users(task_id, user_id);

-- === Users ===
INSERT INTO users (username, email) VALUES
  ('alice', 'alice@example.com'),
  ('bob',   'bob@example.com')
ON CONFLICT (email) DO NOTHING;

-- === Projects ===
INSERT INTO projects (name, description) VALUES
  ('Project A', 'Sample project A'),
  ('Project B', 'Sample project B')
ON CONFLICT (name) DO NOTHING;

-- === Tasks ===
INSERT INTO tasks (title, status, project_id)
SELECT 'Task 1', 'OPEN', p.id
FROM projects p WHERE p.name = 'Project A'
ON CONFLICT (title, project_id) DO NOTHING;

INSERT INTO tasks (title, status, project_id)
SELECT 'Task 2', 'IN_PROGRESS', p.id
FROM projects p WHERE p.name = 'Project A'
ON CONFLICT (title, project_id) DO NOTHING;

INSERT INTO tasks (title, status, project_id)
SELECT 'Task 3', 'DONE', p.id
FROM projects p WHERE p.name = 'Project B'
ON CONFLICT (title, project_id) DO NOTHING;

-- === Task↔User assignments ===
INSERT INTO task_users (task_id, user_id)
SELECT t.id, u.id
FROM tasks t
JOIN projects p ON p.id = t.project_id
JOIN users u ON u.email = 'alice@example.com'
WHERE t.title = 'Task 1' AND p.name = 'Project A'
ON CONFLICT (task_id, user_id) DO NOTHING;

INSERT INTO task_users (task_id, user_id)
SELECT t.id, u.id
FROM tasks t
JOIN projects p ON p.id = t.project_id
JOIN users u ON u.email = 'bob@example.com'
WHERE t.title = 'Task 2' AND p.name = 'Project A'
ON CONFLICT (task_id, user_id) DO NOTHING;
