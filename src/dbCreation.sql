-- Change DB Name, User and Password in dbConnection if desired

create table Users (
    username varchar(255) primary key,
    password varchar(255) not null,
    coins int not null,
    name varchar(255),
    bio varchar(255),
    image varchar(255)
);

create table Stats (
    username varchar(255) not null,
    games_played int default 0 not null,
    games_won int default 0 not null,
    games_lost int default 0 not null,
    elo int default 100 not null,
    FOREIGN KEY (username) references users(username)
);

create table Cards (
    username varchar(255),
    package_id int references packages(id),
    card_id varchar(255) primary key not null,
    name varchar(255) not null,
    damage float not null,
    monster_type boolean not null,
    element_type varchar(50) not null
);

create table Session (
    username varchar(255) not null,
    token varchar(255) not null,
    FOREIGN KEY (username) references users(username)
);

create table Packages (
    id serial primary key,
    card_1 varchar(255) not null,
    card_2 varchar(255) not null,
    card_3 varchar(255) not null,
    card_4 varchar(255) not null,
    card_5 varchar(255) not null,
    bought bool default false not null
);

create table Decks (
    username varchar(255) not null unique,
    card1_id varchar(255),
    card2_id varchar(255),
    card3_id varchar(255),
    card4_id varchar(255)
);

create table Trade (
    trade_id varchar(255) not null,
    card_id varchar(255) not null,
    type varchar(50) not null,
    minDmg int,
    element varchar(50)
);

-- RUN CURL SCRIPT BEFORE THIS:

-- Test battle with following users
-- because the decks provided in the curl script basically always end in a draw

INSERT INTO users (username, password, coins) VALUES ('test1', 'password', 20);
INSERT INTO users (username, password, coins) VALUES ('test2', 'password', 20);
INSERT INTO stats (username, games_played, games_won, games_lost, elo) VALUES ('test1', 0, 0, 0, 100);
INSERT INTO stats (username, games_played, games_won, games_lost, elo) VALUES ('test2', 0, 0, 0, 100);
INSERT INTO session (username, token) VALUES ('test1', 'test1-mtcgToken');
INSERT INTO session (username, token) VALUES ('test2', 'test2-mtcgToken');
INSERT INTO decks (username, card1_id, card2_id, card3_id, card4_id) VALUES ('test1', '845f0dc7-37d0-426e-994e-43fc3ac83c08' , '99f8f8dc-e25e-4a95-aa2c-782823f36e2a', 'e85e3976-7c86-4d06-9a80-641c2019a79f', '171f6076-4eb5-4a7d-b3f2-2d650cc3d237');
INSERT INTO decks (username, card1_id, card2_id, card3_id, card4_id) VALUES ('test2' ,'74635fae-8ad3-4295-9139-320ab89c2844','d6e9c720-9b5a-40c7-a6b2-bc34752e3463','d60e23cf-2238-4d49-844f-c7589ee5342e','02a9c76e-b17d-427f-9240-2dd49b0d3bfd');

/*
curl -i -X POST http://localhost:10001/battles --header "Authorization: Bearer test1-mtcgToken"
curl -i -X POST http://localhost:10001/battles --header "Authorization: Bearer test2-mtcgToken"
 */



-- Empty tables to run curl script again
DELETE FROM cards;
DELETE FROM packages;
DELETE FROM decks;
DELETE FROM session;
DELETE FROM stats;
DELETE FROM users;
DELETE FROM trade