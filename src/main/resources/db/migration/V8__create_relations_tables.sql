CREATE TABLE BookAuthor (
    Book_Author_Id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    Book_Id INTEGER NOT NULL,
    Author_Id INTEGER NOT NULL,
    CONSTRAINT fk_book_id FOREIGN KEY(Book_Id) REFERENCES book(id),
    CONSTRAINT fk_author_id FOREIGN KEY(Author_Id) REFERENCES author(id)
);

CREATE TABLE BookTag (
     Book_Tag_Id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
     Book_Id INTEGER NOT NULL,
     Tag_Id INTEGER NOT NULL,
     CONSTRAINT fk_book_id FOREIGN KEY(Book_Id) REFERENCES book(id),
     CONSTRAINT fk_tag_id FOREIGN KEY(Tag_Id) REFERENCES tag(id)
);

CREATE TABLE BookGenre (
      Book_Genre_Id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
      Book_Id INTEGER NOT NULL,
      Genre_Id INTEGER NOT NULL,
      CONSTRAINT fk_book_id FOREIGN KEY(Book_Id) REFERENCES book(id),
      CONSTRAINT fk_genre_id FOREIGN KEY(Genre_Id) REFERENCES Genre(id)
);

CREATE TABLE BookLanguage (
     Book_Language_Id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
     Book_Id INTEGER NOT NULL,
     Language_Id INTEGER NOT NULL,
     CONSTRAINT fk_book_id FOREIGN KEY(Book_Id) REFERENCES book(id),
     CONSTRAINT fk_language_id FOREIGN KEY(Language_Id) REFERENCES Language(id)
);

CREATE TABLE BookFormat (
      Book_Format_Id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
      Book_Id INTEGER NOT NULL,
      Format_Id INTEGER NOT NULL,
      CONSTRAINT fk_book_id FOREIGN KEY(Book_Id) REFERENCES book(id),
      CONSTRAINT fk_format_id FOREIGN KEY(Format_Id) REFERENCES Format(id)
);

CREATE TABLE BookPublisher (
        Book_Publisher_Id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
        Book_Id INTEGER NOT NULL,
        Publisher_Id INTEGER NOT NULL,
        CONSTRAINT fk_book_id FOREIGN KEY(Book_Id) REFERENCES book(id),
        CONSTRAINT fk_publisher_id FOREIGN KEY(Publisher_Id) REFERENCES Publisher(id)
);