create table users (
    id varchar(100) not null,
    username varchar(100) not null,
    password varchar(100) not null,
    name varchar (100) not null,
    nik varchar (100) not null,
    phone varchar(20) not null,
    email varchar(100) not null,
    roles enum("admin", "customers"),
    primary key(id),
    unique(id, username,nik)
)engine = innodb;