ALTER TABLE users DROP INDEX id;

ALTER TABLE users
    ADD CONSTRAINT uq_users_username UNIQUE (username),
    ADD CONSTRAINT uq_users_nik UNIQUE (nik);