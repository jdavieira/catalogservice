package com.critical.catalogservice.data.repository;

import com.critical.catalogservice.data.entity.Book;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findByIsbn(String isbn);

    List<Book> findByOriginalTitle(String originalTitle);

    List<Book> findByTitle(String title);

    @Query(value = "SELECT b FROM Book b WHERE b.synopsis like CONCAT('%',:synopsis,'%')")
    List<Book> findBySynopsis(@Param("synopsis")String synopsis);

    @Query(value = "SELECT b FROM Book b WHERE b.stockAvailable > 0")
    List<Book> findAllByAvailability();

    List<Book> findAll(@Nullable Specification<Book> spec);
}