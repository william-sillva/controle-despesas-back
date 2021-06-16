CREATE TABLE sistema_001.users (
    id serial       NOT NULL,
    name varchar(20)  NOT NULL,
    profile varchar(50) NOT NULL,
    email varchar(75)  NOT NULL,
    password varchar(255) NOT NULL,
    CONSTRAINT usuario_pk PRIMARY KEY (id)
);
