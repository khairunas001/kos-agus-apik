create table rooms(
    id varchar(100) not null,
    user_id varchar(100) not null,
    title varchar(100) not null,
    availability enum('available','booked') default 'available',
    details text not null,
    price bigint not null,
    created_at timestamp,
    primary key	(id),
    foreign key fk_users_rooms (user_id) references users(id)
)engine = innodb;