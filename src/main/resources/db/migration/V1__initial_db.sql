create table Addresses(
    id long primary key auto_increment,
    index int not null,
    country varchar(64) not null,
    locality varchar(64) not null,
    street varchar(64) not null,
    house varchar(64) not null,
    apartment varchar(64) not null
)