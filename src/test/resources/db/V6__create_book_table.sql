
CREATE TABLE IF NOT EXISTS book (
    Id INTEGER not null AUTO_INCREMENT,
    Title VARCHAR(100) NOT NULL,
    Original_Title VARCHAR(100) NOT NULL,
    ISBN VARCHAR(30) NOT NULL,
    Edition VARCHAR(100) NOT NULL,
    Release_Date DATE NOT NULL,
    Edition_Date DATE NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    Promotional_Price DECIMAL(10, 2),
    Is_Series BOOLEAN NOT NULL,
    Availability SMALLINT NOT NULL,
    Synopsis VARCHAR(1000) NOT NULL,
    Stock_Available INTEGER NOT NULL,
    Created_On TIMESTAMP NOT NULL,
    Updated_On TIMESTAMP  NULL,
    Publisher_Id INTEGER NOT NULL,
    PRIMARY KEY ( Id ),
    FOREIGN KEY (Publisher_Id) REFERENCES publisher(Id)
);