
CREATE TABLE IF NOT EXISTS author (
    Id INTEGER AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    Original_Name VARCHAR(500) NULL,
    Date_Of_Birth DATE NOT NULL,
    Place_Of_Birth VARCHAR(255) NOT NULL,
    Date_Of_Death DATE NULL,
    Place_Of_Death VARCHAR(255) NULL,
    About VARCHAR(500) NULL,
    PRIMARY KEY ( Id )
);