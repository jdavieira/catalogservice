package com.critical.catalogservice.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/db/h2migration/V1__create_author_table.sql", "/db/h2migration/V6__create_book_table.sql", "/db/h2migration/V3__create_bookformat_table.sql", "/db/h2migration/V4__create_genre_table.sql", "/db/h2migration/V5__create_language_table.sql", "/db/h2migration/V2__create_publisher_table.sql", "/db/h2migration/V7__create_tag_table.sql", "/db/h2migration/V8__create_relations_tables.sql"
        })
@DataJpaTest
public class BaseRepositoryTests {
    @Autowired
    TestEntityManager entityManager;
}