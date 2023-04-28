package com.smileksey.movie2watch.services;

import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import com.smileksey.movie2watch.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final TgUserService tgUserService;

    @Autowired
    public MovieService(MovieRepository movieRepository, TgUserService tgUserService) {
        this.movieRepository = movieRepository;
        this.tgUserService = tgUserService;
    }

    @Transactional
    public void save(Movie movie, TgUser tgUser) {
        enrichMovieData(movie, tgUser);
        movieRepository.save(movie);
    }

    public List<Movie> getMoviesAddedByUser(TgUser tgUser) {
        return movieRepository.findAllByAddedByUser(tgUser);
    }

    public Movie getMovieById(int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElse(null);
    }

    public List<Movie> getMoviesAddedByUserIsWatched(TgUser tgUser, boolean isWatched) {
        return movieRepository.findByAddedByUserAndAndIsWatched(tgUser, isWatched);
    }

    @Transactional
    public void changeWatchedStatus(Movie movie, boolean isWatched, TgUser tgUser) {
        movie.setWatched(isWatched);
        save(movie, tgUser);
    }


    @Transactional
    public void deleteMovieById(int id) {
        movieRepository.deleteMovieById(id);
    }


    @Transactional
    public void enrichMovieData(Movie movie, TgUser tgUser) {
        movie.setAddedByUser(tgUser);
        movie.setAddedAt(LocalDateTime.now());
    }
}
