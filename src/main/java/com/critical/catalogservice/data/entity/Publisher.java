package com.critical.catalogservice.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "publisher")
@Getter
@Setter
@NoArgsConstructor
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.PROTECTED)
    private int id;

    @Column(name = "name", nullable = false)
    @Size(max = 50)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher", cascade = CascadeType.ALL)
    List<Book> books;

    public Publisher(int id, String name) {

        this.id = id;
        this.name = name;
    }
}