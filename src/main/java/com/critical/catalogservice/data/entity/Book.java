package com.critical.catalogservice.data.entity;

import com.critical.catalogservice.data.entity.enums.BookAvailability;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.PROTECTED)
    private int id;

    @Column(name = "Title", nullable = false)
    @Size(max = 100)
    private String title;

    @Column(name = "Original_Title", nullable = false)
    @Size(max = 100)
    private String originalTitle;

    @Column(name = "ISBN", nullable = false)
    @Size(max = 30)
    private String isbn;

    @Column(name = "Edition", nullable = false)
    @Size(max = 100)
    private String edition;

    @Column(name = "Synopsis", nullable = false)
    @Size(max = 1000)
    private String synopsis;

    @Column(name = "Is_Series", nullable = false)
    private boolean isSeries;

    @Column(name = "Availability", nullable = false)
    private BookAvailability availability;

    @Column(name = "Release_Date", nullable = false)
    private Date releaseDate;

    @Column(name = "Edition_Date", nullable = false)
    private Date editionDate;

    @Column(name = "Price", nullable = false)
    private Float price;

    @Column(name = "Promotional_Price", nullable = false)
    private Float promotionalPrice;

    @Column(name = "Stock_Available", nullable = false)
    @Min(0)
    private int stockAvailable;

    @Column(name = "Created_On", nullable = false)
    @CreationTimestamp
    @Setter(AccessLevel.PROTECTED)
    private Instant createdOn;

    @Column(name = "Updated_On")
    @Setter(AccessLevel.PROTECTED)
    @UpdateTimestamp
    private Instant updatedOn;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "bookauthor", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    List<Author> authors;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "booklanguage", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "language_id"))
    List<Language> languages;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "bookgenre", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    List<Genre> genres;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "booktag", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    List<Tag> tags;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "bookformat", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "format_id"))
    List<Format> formats;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "publisher_id")
    Publisher publisher;

    public Book( String title, String originalTitle, String isbn, String edition, String synopsis, boolean isSeries,
                 BookAvailability availability, Date releaseDate, Date editionDate, Float price, Float promotionalPrice,
                 int stockAvailable, List<Author> authors, List<Language> languages, List<Genre> genres, List<Tag> tags,
                 List<Format> formats, Publisher publisher) {

        this.title = title;
        this.originalTitle = originalTitle;
        this.isbn = isbn;
        this.edition = edition;
        this.synopsis = synopsis;
        this.isSeries = isSeries;
        this.availability = availability;
        this.releaseDate = releaseDate;
        this.editionDate = editionDate;
        this.price = price;
        this.promotionalPrice = promotionalPrice;
        this.stockAvailable = stockAvailable;
        this.authors = authors;
        this.languages = languages;
        this.genres = genres;
        this.tags = tags;
        this.formats = formats;
        this.publisher = publisher;
    }
}