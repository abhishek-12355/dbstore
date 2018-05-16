drop database if exists dbstore;
create database dbstore;

use dbstore;

drop table if exists ns_dbstore;
create table ns_dbstore (
    store_id varchar(64) not null,
    type varchar(20),
    payload blob not null,
    created datetime not null,
    primary key (store_id)
);

drop table if exists ns_correlation;
create table ns_correlation (
  provider_id varchar(64) not null,
  provider_entity_id varchar(64) not null,
  consumer_id varchar(64),
  consumer_entity_id varchar(64),
  correlation_data varchar(4000),
  primary key `uk_provider_consumer` (provider_id, provider_entity_id)
);