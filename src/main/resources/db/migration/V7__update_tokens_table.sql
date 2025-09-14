ALTER TABLE tokens DROP INDEX token;

ALTER TABLE tokens
    ADD CONSTRAINT uq_tokens_token UNIQUE (token);