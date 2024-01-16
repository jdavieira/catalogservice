CREATE  TABLE author (
    Id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    Name VARCHAR(255) NOT NULL,
    Original_Name VARCHAR(500) NULL,
    Date_Of_Birth DATE NOT NULL,
    Place_Of_Birth VARCHAR(255) NOT NULL,
    Date_Of_Death DATE NULL,
    Place_Of_Death VARCHAR(255) NULL,
    About VARCHAR(500) NULL
);

CREATE INDEX IDX_AUTHOR_NAME ON author(Name);
CREATE INDEX IDX_AUTHOR_DATE_OF_BIRTH ON author(Date_Of_Birth);
CREATE INDEX IDX_AUTHOR_NAME_DATE_OF_BIRTH ON author(Name, Date_Of_Birth);