package com.smileksey.movie2watch.cache;

/*Критерии для поиска фильмов, указанные пользователем*/
public class UserChoiceData {
    private String genre;
    private String year;
    private String rating;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
