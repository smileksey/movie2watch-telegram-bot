package com.smileksey.movie2watch.models.kinopoiskmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.models.TgUserMovie;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "alternative_name")
    private String alternativeName;
    @Column(name = "year")
    private String year;
    @Column(name = "description")
    private String description;
    @Transient
    private Poster poster;
    @Transient
    private List<Country> countries;
    @Transient
    private List<Genre> genres;
    @Transient
    private Video videos;
    @Transient
    private Rating rating;
    @Transient
    private boolean isSeries;

    //для связи many-to-many с TgUser
    @OneToMany(mappedBy = "movie")
    List<TgUserMovie> savedMovies;

    public Movie() {
    }

    public Movie(int id, String name, String alternativeName, String year, String description) {
        this.id = id;
        this.name = name;
        this.alternativeName = alternativeName;
        this.year = year;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public void setAlternativeName(String alternativeName) {
        this.alternativeName = alternativeName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Poster getPoster() {
        return poster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Video getVideos() {
        return videos;
    }

    public void setVideos(Video videos) {
        this.videos = videos;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public List<TgUserMovie> getSavedMovies() {
        return savedMovies;
    }

    public void setSavedMovies(List<TgUserMovie> savedMovies) {
        this.savedMovies = savedMovies;
    }

    @JsonProperty(value = "isSeries")
    public boolean isSeries() {
        return isSeries;
    }

    public void setSeries(boolean series) {
        isSeries = series;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", isSeries=" + isSeries +
                '}';
    }
}
