package com.smileksey.movie2watch.cache;

import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/*Не используется*/
@Component
public class MovieCache {

    private Map<Integer, Movie> foundMovies = new HashMap<>();

    public void putMovie(int movieId, Movie movie) {
        foundMovies.put(movieId, movie);
    }

    public Movie getMovie(int movieId) {
        return foundMovies.get(movieId);
    }

    public void deleteMovie(int movieId) {
        foundMovies.remove(movieId);
    }
}
