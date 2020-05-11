create sequence hibernate_sequence start 1 increment 1;
create table media_items
(
    id                int8   not null,
    created_at        timestamp,
    updated_at        timestamp,
    backdrop_path     varchar(255),
    imdb_id           varchar(255),
    media_id          varchar(255),
    original_language varchar(255),
    original_title    varchar(255),
    popularity        float8 not null,
    poster_path       varchar(255),
    release_date      date,
    title             varchar(255),
    tmdb_id           int4   not null,
    type              int4,
    vote_average      float8 not null,
    vote_count        int4   not null,
    primary key (id)
);
create table vod_items
(
    id            int8 not null,
    provider      varchar(255),
    media_item_id int8,
    primary key (id)
);
create table watchlist_items
(
    id           int8 not null,
    created_at   timestamp,
    updated_at   timestamp,
    media_id     int8,
    watchlist_id int8,
    primary key (id)
);
create table watchlists
(
    id         int8 not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);
alter table if exists vod_items
    add constraint fk_vod_item_media_item foreign key (media_item_id) references media_items;
alter table if exists watchlist_items
    add constraint fk_watchlist_item_media_item foreign key (media_id) references media_items;
alter table if exists watchlist_items
    add constraint fk_watchlist_item_watchlist foreign key (watchlist_id) references watchlists;
