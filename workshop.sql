create database workshop;
use workshop;

create table account (account_id int primary key auto_increment , customer_id int, account_type varchar(50) ,
 balance decimal(15,2) not null, created_at timestamp default current_timestamp , address varchar(100),mobile varchar(20) );
 select * from account;
 # truncate table account;
 
 create table savings_account (account_id int primary key,foreign key(account_id) references account(account_id),interest_rate decimal(5,2) not null);
 select * from savings_account;
 
 
 create table current_account(account_id int primary key,foreign key(account_id) references account(account_id), overdraft_limit decimal(15,2) NOT NULL) ;
 select * from current_account;
 
 
 create table transaction (transaction_id int primary key auto_increment, 
 account_id int,foreign key(account_id) references account(account_id),
 transaction_type varchar(50) not null,
 amount decimal(15,2)not null,
 transaction_date timestamp default current_timestamp);
 select * from transaction;