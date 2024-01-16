package com.critical.catalogservice.data.specification;

import com.critical.catalogservice.data.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecifications {

    public static Specification<Book> hasBooleanProperty(Boolean value, String fieldName ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(fieldName), value);
    }


    public static Specification<Book> hasIntProperty(int value, String fieldName ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(fieldName), value);
    }

    public static Specification<Book> hasFloatPropertyGreaterThan(Double value, String fieldName ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), value);
    }

    public static Specification<Book> hasFloatPropertyLessThan(Double value, String fieldName ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), value);
    }

    public static Specification<Book> hasFloatPropertyEqualTo(Double value, String fieldName ) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(fieldName), value);
    }

    public static Specification<Book> hasTagEqualTo(String value) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, Tag> tagJoin = root.join("tags", JoinType.INNER);

            return criteriaBuilder.equal(tagJoin.get("name"), value);
        };
    }

    public static Specification<Book> hasGenreEqualTo(String value) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, Genre> gerneJoin = root.join("genres", JoinType.INNER);

            return criteriaBuilder.equal(gerneJoin.get("name"), value);
        };
    }

    public static Specification<Book> hasAuthorEqualTo(String value) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, Author> authorJoin = root.join("authors", JoinType.INNER);

            return criteriaBuilder.equal(authorJoin.get("name"), value);
        };
    }

    public static Specification<Book> hasLanguageEqualTo(String value) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, Language> languageJoin = root.join("languages", JoinType.INNER);

            return criteriaBuilder.equal(languageJoin.get("name"), value);
        };
    }
}