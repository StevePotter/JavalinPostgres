CREATE TABLE users (
    id serial PRIMARY KEY,
    email VARCHAR UNIQUE NOT NULL,
    password VARCHAR NOT NULL,
    created_on TIMESTAMP NOT NULL
);