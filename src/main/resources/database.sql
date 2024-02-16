create table measurement(
    id int primary key generated by default as identity,
    value double precision not null ,
    is_raining boolean not null ,
    sensor_name varchar(100) not null references sensor(name),
    time timestamp not null ,
);

create table sensor(
    id int primary key generated by default as identity,
    name varchar(100) unique not null ,
);

insert into sensor values
    ('test1'),
    ('test2'),
    ('test3');