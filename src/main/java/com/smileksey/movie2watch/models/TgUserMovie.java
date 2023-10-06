package com.smileksey.movie2watch.models;

import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_movie")
public class TgUserMovie {
    @EmbeddedId
    private TgUserMovieKey id;

    @ManyToOne
    @MapsId("tgUserId")
    @JoinColumn(name = "tg_user_id")
    private TgUser tgUser;

    @ManyToOne
    @MapsId("tgUserId")
    @JoinColumn(name = "movie_id")
    Movie movie;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @Column(name = "is_watched")
    private boolean isWatched;

    @Column(name = "user_rating")
    private int userRating;

    public TgUserMovie() {
    }

    public TgUserMovie(TgUserMovieKey id, TgUser tgUser, Movie movie, LocalDateTime addedAt,
                       boolean isWatched, int userRating) {
        this.id = id;
        this.tgUser = tgUser;
        this.movie = movie;
        this.addedAt = addedAt;
        this.isWatched = isWatched;
        this.userRating = userRating;
    }

    public TgUserMovieKey getId() {
        return id;
    }

    public void setId(TgUserMovieKey id) {
        this.id = id;
    }

    public TgUser getTgUser() {
        return tgUser;
    }

    public void setTgUser(TgUser tgUser) {
        this.tgUser = tgUser;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
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
}
