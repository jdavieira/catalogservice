package com.critical.catalogservice.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "format")
@Getter
@Setter
@NoArgsConstructor
public class Format {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.PROTECTED)
    private int id;

    @Column(name = "name", nullable = false)
    @Size(max = 50)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "formats")
    List<Book> books;

    public Format(int id, String name) {

        this.id = id;
        this.name = name;
    }
}