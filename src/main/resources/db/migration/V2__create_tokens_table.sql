create table tokens (
    id varchar(100) not null,
    user_id varchar(100) not null,
    token varchar(100) not null,
    token_expired_at timestamp,
    primary key	(id),
    unique(token),
    foreign key fk_users_tokens (user_id) references users(id)
)engine = innodb;