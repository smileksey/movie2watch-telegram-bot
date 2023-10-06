package com.smileksey.movie2watch.models;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public class UserMovie {
    @ManyToOne
    @JoinColumn(name = "added_by_user_id", referencedColumnName = "id")
    private TgUser addedByUser;
    @Column(name = "added_at")
    private LocalDateTime addedAt;
    @Column(name = "is_watched")
    private boolean isWatched;
    @Column(name = "user_rating")
    private int userRating;
}
