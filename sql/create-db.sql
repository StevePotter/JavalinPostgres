CREATE TABLE users (
    id serial PRIMARY KEY,
    email VARCHAR UNIQUE NOT NULL,
    password_hash VARCHAR NOT NULL,
    password_salt VARCHAR NOT NULL,
    api_token VARCHAR UNIQUE NULL,
    created_on TIMESTAMP NOT NULL
);