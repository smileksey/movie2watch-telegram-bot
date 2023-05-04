package com.smileksey.movie2watch.models;

import jakarta.persistence.*;

/*Критерии для поиска фильмов, указанные пользователем*/
@Entity
@Table(name = "user_choice_data")
public class UserChoiceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "genre")
    private String genre;
    @Column(name = "year")
    private String year;
    @Column(name = "rating")
    private String rating;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private TgUser tgUser;

    public UserChoiceData() {
    }

    public UserChoiceData(String genre, String year, String rating, TgUser tgUser) {
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.tgUser = tgUser;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TgUser getTgUser() {
        return tgUser;
    }

    public void setTgUser(TgUser tgUser) {
        this.tgUser = tgUser;
    }

    @Override
    public String toString() {
        return "UserChoiceData{" +
                "genre='" + genre + '\'' +
                ", year='" + year + '\'' +
                ", rating='" + rating + '\'' +
                ", tgUser=" + tgUser +
                '}';
    }
}
