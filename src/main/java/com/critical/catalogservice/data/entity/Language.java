package com.critical.catalogservice.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "language")
@Getter
@Setter
@NoArgsConstructor
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    @Size(max = 150)
    private String name;

    @Column(name = "Culture", nullable = false)
    @Size(max = 5)
    private String culture;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "languages")
    List<Book> books;

    public Language(String name, String culture) {

        this.name = name;
        this.culture = culture;
    }
}