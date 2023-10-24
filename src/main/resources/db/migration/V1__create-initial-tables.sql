create table users (
    id bigserial not null,
    nickname varchar(255),
    email varchar(255) unique,
    password varchar(255),
    active boolean,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    primary key (id)
);
create table users_profiles (
    profile integer,
    user_id bigint not null
);
alter table if exists users_profiles add constraint FKgmc2q50iqwetlbaqcchgvks8t foreign key (user_id) references users;
