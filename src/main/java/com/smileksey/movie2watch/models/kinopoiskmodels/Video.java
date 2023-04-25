package com.smileksey.movie2watch.models.kinopoiskmodels;

import java.util.List;

public class Video {
    private List<Trailer> trailers;

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
