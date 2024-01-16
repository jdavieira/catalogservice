CREATE TABLE IF NOT EXISTS Language (
    Id int not null AUTO_INCREMENT,
    Name VARCHAR(150) NOT NULL,
    Culture VARCHAR(5) NOT NULL,
    PRIMARY KEY ( Id )
);