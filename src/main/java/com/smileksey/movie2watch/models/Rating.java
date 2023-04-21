package com.smileksey.movie2watch.models;

public class Rating {
    private double imdb;

    public Rating(double imdb) {
        this.imdb = imdb;
    }

    public Rating() {
    }

    public double getImdb() {
        return imdb;
    }

    public void setImdb(double imdb) {
        this.imdb = imdb;
    }
}
