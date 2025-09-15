ALTER TABLE rooms
MODIFY COLUMN created_at timestamp not null default CURRENT_TIMESTAMP;
