create table transactions(
    id varchar(100) not null,
    user_id varchar(100) not null,
    room_id varchar(100) not null,
    amount bigint not null,
    period date not null,
    payment_date bigint not null,
    payment_status enum ('pending','paid','cancelled') default 'pending' not null,
    payment_method varchar(50),
    primary key	(id),
    foreign key fk_users_transactions (user_id) references users(id),
    foreign key fk_rooms_transactions (room_id) references rooms(id)
)engine = innodb;
