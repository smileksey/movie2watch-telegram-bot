package com.smileksey.movie2watch.repositories;

import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.models.TgUserMovie;
import com.smileksey.movie2watch.models.TgUserMovieKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TgUserMovieRepository extends JpaRepository<TgUserMovie, TgUserMovieKey> {
    Optional<List<TgUserMovie>> findAllByTgUserAndIsWatched(TgUser user, boolean isWatched);
    void deleteById(TgUserMovieKey id);
}
