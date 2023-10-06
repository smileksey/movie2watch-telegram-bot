package com.smileksey.movie2watch.services;

import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.models.TgUserMovie;
import com.smileksey.movie2watch.models.TgUserMovieKey;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import com.smileksey.movie2watch.repositories.MovieRepository;
import com.smileksey.movie2watch.repositories.TgUserMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final TgUserMovieRepository userMovieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, TgUserMovieRepository userMovieRepository) {
        this.movieRepository = movieRepository;
        this.userMovieRepository = userMovieRepository;
    }


    @Transactional
    public void save(Movie movie, TgUser tgUser) {

        movieRepository.save(movie);
        userMovieRepository.save(createTgUserMovieEntity(movie, tgUser));
    }

    @Transactional
    public void saveTgUserMovie(TgUserMovie userMovie) {
        userMovieRepository.save(userMovie);
    }


    public Movie getMovieById(int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElse(null);
    }

    public TgUserMovie getUserMovie(long userId, int movieId) {
        Optional<TgUserMovie> userMovie = userMovieRepository.findById(new TgUserMovieKey(userId, movieId));
        return userMovie.orElse(null);
    }

    public List<Movie> getMoviesAddedByUserIsWatched(TgUser tgUser, boolean isWatched) {

        List<TgUserMovie> userMovies = userMovieRepository.findAllByIsWatched(isWatched);
        return userMovies.stream().map(userMovie -> userMovie.getMovie()).collect(Collectors.toList());
    }

    @Transactional
    public void changeWatchedStatus(TgUserMovie userMovie, boolean isWatched) {

        userMovie.setWatched(isWatched);
        saveTgUserMovie(userMovie);
    }

    //TODO
    @Transactional
    public void deleteMovieById(int id) {
        movieRepository.deleteMovieById(id);
    }

    @Transactional
    public TgUserMovie createTgUserMovieEntity(Movie movie, TgUser tgUser) {

        TgUserMovie userMovie = new TgUserMovie();

        userMovie.setId(new TgUserMovieKey(tgUser.getId(), movie.getId()));
        userMovie.setTgUser(tgUser);
        userMovie.setMovie(movie);
        userMovie.setAddedAt(LocalDateTime.now());

        return userMovie;
    }
}
