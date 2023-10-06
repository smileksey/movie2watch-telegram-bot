package com.smileksey.movie2watch.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class TgUserMovieKey implements Serializable {
    @Column(name = "tg_user_id")
    private long tgUserId;
    @Column(name = "movie_id")
    private int movieId;

    public TgUserMovieKey(long tgUserId, int movieId) {
        this.tgUserId = tgUserId;
        this.movieId = movieId;
    }

    public TgUserMovieKey() {
    }

    public long getTgUserId() {
        return tgUserId;
    }

    public void setTgUserId(long tgUserId) {
        this.tgUserId = tgUserId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TgUserMovieKey that = (TgUserMovieKey) o;

        if (tgUserId != that.tgUserId) return false;
        return movieId == that.movieId;
    }

    @Override
    public int hashCode() {
        int result = (int) (tgUserId ^ (tgUserId >>> 32));
        result = 31 * result + movieId;
        return result;
    }
}
