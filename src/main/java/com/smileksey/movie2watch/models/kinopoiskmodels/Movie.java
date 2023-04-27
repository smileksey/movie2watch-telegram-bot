package com.smileksey.movie2watch.models.kinopoiskmodels;

import com.smileksey.movie2watch.models.TgUser;
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
    @ManyToOne
    @JoinColumn(name = "added_by_user_id", referencedColumnName = "id")
    private TgUser addedByUser;
    @Column(name = "added_at")
    private LocalDateTime addedAt;
    @Column(name = "is_watched")
    private boolean isWatched;
    @Column(name = "user_rating")
    private int userRating;

    public Movie() {
    }

    public Movie(int id, String name, String alternativeName, String year, String description, TgUser addedByUser, LocalDateTime addedAt, boolean isWatched, int userRating) {
        this.id = id;
        this.name = name;
        this.alternativeName = alternativeName;
        this.year = year;
        this.description = description;
        this.addedByUser = addedByUser;
        this.addedAt = addedAt;
        this.isWatched = isWatched;
        this.userRating = userRating;
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

    public TgUser getAddedByUser() {
        return addedByUser;
    }

    public void setAddedByUser(TgUser addedByUser) {
        this.addedByUser = addedByUser;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
