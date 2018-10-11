create table account (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  constraint pk_account primary key (id)
);

create table transactions (
  id                            bigint auto_increment not null,
  transaction_type              varchar(255) not null,
  account_id                    bigint not null,
  amount                        decimal(38) not null,
  status                        varchar(255) not null,
  constraint pk_transactions primary key (id)
);

create table vault (
  id                            bigint auto_increment not null,
  account_id                    bigint not null,
  amount                        decimal(38) not null,
  constraint pk_vault primary key (id)
);

