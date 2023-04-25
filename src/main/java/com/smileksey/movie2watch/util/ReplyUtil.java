package com.smileksey.movie2watch.util;

import com.smileksey.movie2watch.models.Movie;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Component
public class ReplyUtil {

    public SendMessage startReply(long chatId) {
        String answer = "Привет! Воспользуйся главным меню!";
        return textReply(chatId, answer);
    }

    public SendMessage textReply(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        return sendMessage;
    }

    public SendPhoto imageReply(long chatId, Movie movie) {
        if (movie.getPoster() != null) {
            SendPhoto sendPhotoRequest = new SendPhoto();
            sendPhotoRequest.setChatId(chatId);
            sendPhotoRequest.setPhoto(new InputFile(movie.getPoster().getUrl()));

            return  sendPhotoRequest;
        }
        return null;
    }

    public String movieDescription(Movie movie) {

        if (movie != null) {
            StringBuilder builder = new StringBuilder();

            builder.append(movie.getName());

            if (movie.getAlternativeName() != null) {
                builder.append(" / ").append(movie.getAlternativeName());
            }

            builder.append("\n")
                    .append(movie.getYear()).append("\n")
                    .append(movie.getCountries()).append("\n")
                    .append("\n")
                    .append("Жанр: ").append(movie.getGenres()).append("\n");

            if (movie.getRating() != null) {
                builder
                        .append("\n")
                        .append("Рейтинг IMDb: ").append(movie.getRating().getImdb()).append("\n")
                        .append("Рейтинг Кинопоиск: ").append(movie.getRating().getKp()).append("\n");
            }

            builder.append("\n").append(movie.getDescription()).append("\n");

            if (movie.getPoster() != null && movie.getPoster().getUrl() != null) {
                builder.append(movie.getPoster().getUrl()).append("\n");
            }

            if (movie.getVideos() != null && !movie.getVideos().getTrailers().isEmpty()) {
                builder.append("\n").append("Трейлер: ").append(movie.getVideos().getTrailers().get(0).getUrl());
            }

            return builder.toString();

        } else return "Произошла ошибка. Попробуйте снова.";
    }


}
