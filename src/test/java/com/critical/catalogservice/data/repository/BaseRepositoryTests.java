package com.critical.catalogservice.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

@Sql({
        "/db/V1__create_author_table.sql", "/db/V6__create_book_table.sql",
        "/db/V3__create_bookformat_table.sql",
        "/db/V4__create_genre_table.sql",
        "/db/V5__create_language_table.sql", "/db/V2__create_publisher_table.sql",
        "/db/V7__create_tag_table.sql",
        "/db/V8__create_relations_tables.sql"
        })
@DataJpaTest
public class BaseRepositoryTests {
    @Autowired
    TestEntityManager entityManager;
}