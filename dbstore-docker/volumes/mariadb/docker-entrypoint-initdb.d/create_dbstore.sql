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
  consumer_id varchar(64) not null,
  consumer_entity_id varchar(64) not null,
  correlation_data varchar(4000) not null,
  primary key `uk_provider_consumer` (provider_id, provider_entity_id, consumer_id, consumer_entity_id)
);