package com.critical.catalogservice.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor
public class Author {

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "authors")
    List<Book> books;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    @Size(max = 255)
    private String name;

    @Column(name = "original_name", nullable = false)
    @Size(max = 500)
    private String originalName;

    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "place_of_birth", nullable = false)
    @Size(max = 255)
    private String placeOfBirth;

    @Column(name = "date_of_death")
    private Date dateOfDeath;

    @Column(name = "place_of_death")
    @Size(max = 255)
    private String placeOfDeath;

    @Column(name = "about")
    @Size(max = 500)
    private String about;

    public Author(
            String name, String originalName, Date dateOfBirth, String placeOfBirth, Date dateOfDeath, String placeOfDeath, String about) {

        this.name = name;
        this.originalName = originalName;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.dateOfDeath = dateOfDeath;
        this.placeOfDeath = placeOfDeath;
        this.about = about;
    }
}