create table Refresh_Tokens(
    id long primary key auto_increment,
    token longtext not null,
    new_token longtext
);