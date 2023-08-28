create table Users(
    id long primary key auto_increment,
    username varchar(64) not null unique ,
    password varchar(256) not null,
    authorities varchar(64) array not null,
    last_name varchar(64),
    first_name varchar(64) not null,
    middle_name varchar(64),
    e_mail varchar(64) not null unique,
    phone_number varchar(32),
    gender varchar(32) not null,
    birthdate date not null,
    registration_date date not null,
    address_id long not null
)