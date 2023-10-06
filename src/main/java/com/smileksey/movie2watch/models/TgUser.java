package com.smileksey.movie2watch.models;

import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tg_user")
public class TgUser {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "chat_id")
    private long chatId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @OneToOne(mappedBy = "tgUser")
    private UserChoiceData userChoiceData;
    @OneToMany(mappedBy = "addedByUser")
    private List<Movie> addedMovies;
    @Column(name = "is_subscribed")
    private boolean isSubscribed;

    public TgUser() {
    }

    public TgUser(long id, long chatId, String userName, String firstName, String lastName, boolean isSubscribed) {
        this.id = id;
        this.chatId = chatId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isSubscribed = isSubscribed;
    }

    public TgUser(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserChoiceData getUserChoiceData() {
        return userChoiceData;
    }

    public void setUserChoiceData(UserChoiceData userChoiceData) {
        this.userChoiceData = userChoiceData;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
