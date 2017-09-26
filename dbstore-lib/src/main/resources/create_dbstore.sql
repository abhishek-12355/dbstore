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
  correlation_id varchar(64) not null,
  provider_id varchar(64) not null,
  bucket varchar(64) not null,
  consumer_id varchar(64) not null,
  correlation_data varchar(4000) not null,
  primary key (correlation_id),
  unique key `uk_provider_consumer` (provider_id, bucket, consumer_id)
);