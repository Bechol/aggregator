create schema if not exists agr;

create table if not exists request(
    task_id uuid not null,
    task_type varchar(50) not null,
    s3_key varchar(255) not null,
    result_s3_key varchar(255),
    contains_pd boolean,
    creation_time timestamp not null,
    last_update_time timestamp not null,
    status varchar(50),
    deleted boolean,
    version bigint,
    primary key(task_id)
);