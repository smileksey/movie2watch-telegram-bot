package com.smileksey.movie2watch.models.kinopoiskmodels;

public class Rating {
    private double imdb;
    private double kp;

    public Rating(double imdb, double kp) {
        this.imdb = imdb;
        this.kp = kp;
    }

    public Rating() {
    }

    public double getImdb() {
        return imdb;
    }

    public void setImdb(double imdb) {
        this.imdb = imdb;
    }

    public double getKp() {
        return kp;
    }

    public void setKp(double kp) {
        this.kp = kp;
    }
}
