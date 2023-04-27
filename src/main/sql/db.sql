create table tg_user (
                         id bigint primary key,
                         user_name varchar(100) not null unique,
                         first_name varchar(100),
                         last_name varchar(100)
);

create table user_choice_data (
                                id int generated by default as identity primary key,
                                  user_id bigint references tg_user(id) on delete restrict,
                                  genre varchar(50),
                                  year varchar(30),
                                  rating varchar(4)
);

create table movie (
                       id int primary key,
                       name varchar,
                       alternative_name varchar,
                       year varchar,
                       description varchar,
                       added_by_user_id bigint references tg_user(id) on delete restrict,
                       added_at timestamp,
                       is_watched boolean,
                       user_rating int check (user_rating >= 0)
);