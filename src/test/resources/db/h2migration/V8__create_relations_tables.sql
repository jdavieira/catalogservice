CREATE TABLE IF NOT EXISTS BookAuthor (
    Book_Author_Id INTEGER AUTO_INCREMENT,
    Book_Id INTEGER NOT NULL,
    Author_Id INTEGER NOT NULL,
    CONSTRAINT fk_book_author_id FOREIGN KEY(Book_Id) REFERENCES book(id),
    CONSTRAINT fk_author_id FOREIGN KEY(Author_Id) REFERENCES author(id),
    PRIMARY KEY ( Book_Author_Id )
);

CREATE TABLE IF NOT EXISTS BookTag (
     Book_Tag_Id INTEGER AUTO_INCREMENT,
     Book_Id INTEGER NOT NULL,
     Tag_Id INTEGER NOT NULL,
     CONSTRAINT fk_book_tag_id FOREIGN KEY(Book_Id) REFERENCES book(id),
     CONSTRAINT fk_tag_id FOREIGN KEY(Tag_Id) REFERENCES tag(id),
     PRIMARY KEY ( Book_Tag_Id )
);

CREATE TABLE IF NOT EXISTS BookGenre (
      Book_Genre_Id INTEGER AUTO_INCREMENT,
      Book_Id INTEGER NOT NULL,
      Genre_Id INTEGER NOT NULL,
      CONSTRAINT fk_book_genre_id FOREIGN KEY(Book_Id) REFERENCES book(id),
      CONSTRAINT fk_genre_id FOREIGN KEY(Genre_Id) REFERENCES Genre(id),
      PRIMARY KEY ( Book_Genre_Id )
);

CREATE TABLE IF NOT EXISTS BookLanguage (
     Book_Language_Id INTEGER AUTO_INCREMENT,
     Book_Id INTEGER NOT NULL,
     Language_Id INTEGER NOT NULL,
     CONSTRAINT fk_book_language_id FOREIGN KEY(Book_Id) REFERENCES book(id),
     CONSTRAINT fk_language_id FOREIGN KEY(Language_Id) REFERENCES Language(id),
     PRIMARY KEY ( Book_Language_Id )
);

CREATE TABLE IF NOT EXISTS BookFormat (
      Book_Format_Id INTEGER AUTO_INCREMENT,
      Book_Id INTEGER NOT NULL,
      Format_Id INTEGER NOT NULL,
      CONSTRAINT fk_book_format_id FOREIGN KEY(Book_Id) REFERENCES book(id),
      CONSTRAINT fk_format_id FOREIGN KEY(Format_Id) REFERENCES Format(id),
      PRIMARY KEY ( Book_Format_Id )
);

CREATE TABLE IF NOT EXISTS BookPublisher (
        Book_Publisher_Id INTEGER AUTO_INCREMENT,
        Book_Id INTEGER NOT NULL,
        Publisher_Id INTEGER NOT NULL,
        CONSTRAINT fk_book_publisher_id FOREIGN KEY(Book_Id) REFERENCES book(id),
        CONSTRAINT fk_publisher_id FOREIGN KEY(Publisher_Id) REFERENCES Publisher(id),
        PRIMARY KEY ( Book_Publisher_Id )
);