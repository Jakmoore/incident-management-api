create table monitors
(
    id               bigserial primary key,
    name             varchar(100) not null,
    url              varchar(500) not null,
    expected_status  int          not null,
    interval_seconds int          not null,
    active           boolean      not null default true,
    created_at       timestamp    not null default now()
);