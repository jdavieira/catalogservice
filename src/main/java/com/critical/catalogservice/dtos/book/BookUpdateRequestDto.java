package com.critical.catalogservice.dtos.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Date;

public class BookUpdateRequestDto {

    @NotNull
    @Size(min = 1, max = 100)
    public String title;

    @NotNull
    @Size(min = 1, max = 100)
    public String originalTitle;

    @NotNull
    @Size(min = 1, max = 30)
    public String isbn;

    @NotNull
    @Size(min = 1, max = 100)
    public String edition;

    @NotNull
    @Size(min = 1, max = 100)
    public String synopsis;

    @NotNull
    public boolean isSeries;

    @NotNull
    public BookAvailabilityDto availability;

    @NotNull
    public Date releaseDate;

    @NotNull
    public Date editionDate;

    @NotNull
    @Min(1)
    public Float price;

    @NotNull
    public Float promotionalPrice;

    @NotNull
    @Min(0)
    public int stockAvailable;
}