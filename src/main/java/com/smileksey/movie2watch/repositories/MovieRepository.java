package com.smileksey.movie2watch.repositories;

import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findAllByAddedByUser(TgUser tgUser);

    List<Movie> findByAddedByUserAndAndIsWatched(TgUser tgUser, boolean isWatched);
    void deleteMovieById(int id);
}
