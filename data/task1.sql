-- Write your Task 1 answers in this file

use paf24;



drop table if exists reviews;
drop table if exists bookings;
drop table if exists users;

create table users (
    email varchar(128) not null,
    name varchar(128) not null,
    constraint pk_users primary key (email)
);


create table bookings (
    booking_id char(8) not null,
    listing_id varchar(20) not null,
    duration int not null,
    email varchar(128) not null,
    constraint pk_bookings primary key (booking_id),
    constraint fk_bookings foreign key (email) references users(email) ON DELETE CASCADE
    
);


create table reviews (
    id int not null auto_increment,
    date timestamp not null,
    listing_id varchar(20) not null,
    reviewer_name varchar(64) not null,
    comments text,
    constraint pk_reviews primary key (id)



);



insert into users (
    email, name
)
values 
("fred@gmail.com", "Fred Flintstone"),
("barney@gmail.com", "Barney Rubble"),
("fry@planetexpress.com", "Philip J Fry"),
("hlmer@gmail.com", "Homer Simpson");