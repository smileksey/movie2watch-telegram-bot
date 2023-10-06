package com.smileksey.movie2watch.services;

import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import com.smileksey.movie2watch.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    public void save(Movie movie, TgUser tgUser) {
        enrichMovieData(movie, tgUser);
        movieRepository.save(movie);
    }

    //TODO
    public List<Movie> getMoviesAddedByUser(TgUser tgUser) {
        //return movieRepository.findAllByAddedByUser(tgUser);
        return null;
    }

    public Movie getMovieById(int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElse(null);
    }

    //TODO
    public List<Movie> getMoviesAddedByUserIsWatched(TgUser tgUser, boolean isWatched) {
        //return movieRepository.findByAddedByUserAndAndIsWatched(tgUser, isWatched);
        return null;
    }

    //TODO
    @Transactional
    public void changeWatchedStatus(Movie movie, boolean isWatched, TgUser tgUser) {
        //TODO поле isWatched переехало в класс UserMovie
        //movie.setWatched(isWatched);

        save(movie, tgUser);
    }

    @Transactional
    public void deleteMovieById(int id) {
        movieRepository.deleteMovieById(id);
    }

    //TODO
    @Transactional
    public void enrichMovieData(Movie movie, TgUser tgUser) {

        //TODO 'addedByUser' переместилось в класс UserMovie
        //movie.setAddedByUser(tgUser);

        //TODO 'addedAt' переместилось в класс UserMovie
        //movie.setAddedAt(LocalDateTime.now());
    }
}
