package com.smileksey.movie2watch.util;

import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.List;

@Component
public class ReplyUtil {

    public SendMessage startReply(long chatId) {
        String reply = "Привет! Чтобы начать, нажми 'Подобрать фильм' или воспользуйся меню.\n" +
                        "Для справки нажми /help";
        return textReply(chatId, reply);
    }

    public SendMessage helpReply(long chatId) {
        String reply = "Для поиска фильма воспользуйся кнопокой 'Подобрать фильм'.\n\n" +
                "Чтобы найти фильм с учетом твоих предпочтений, сначала укажи их, нажав /preferences. Список доступных жанров - /genres\n\n" +
                "Понравившиеся фильмы можно сохранять. Для этого под найденным фильмом нажми кнопку 'Добавить в избранное'.\n\n" +
                "Список сохраненных фильмов можно посмотреть по команде /watchlater.\n\n" +
                "Фильмы в этом списке можно отметить как просмотренные с помощью соответствующей кнопки.\n\n" +
                "Список просмотренных фильмов можно увидеть по команде /watched.\n\n" +
                "По команде /subscribe можно подписаться на персональную рассылку. Ближе к вечеру каждую пятницу и субботу я буду присылать тебе " +
                "подборку из нескольких фильмов с учетом твоих предпочтнений. Кстати, не забудь заранее указазать их командой /preferences!\n" +
                "Отписаться от рассылки можно командой /unsubscribe.";
        return textReply(chatId, reply);
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

            if (movie.isSeries()) {
                builder.append("\n")
                        .append("\n")
                        .append("Сериал");
            }

            builder.append("\n")
                    .append("\n")
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

    public String moviesList(List<Movie> movies) {
        StringBuilder builder = new StringBuilder();
        int count = 1;

        for (Movie movie : movies) {
            builder.append(count).append(") ")
                    .append(movie.getName());

            if (movie.getAlternativeName() != null) {
                builder.append(" / ")
                        .append(movie.getAlternativeName());
            }

            builder.append("\n")
                    .append(movie.getYear())
                    .append("\n")
                    .append(UrlBuilder.getKinopoiskUrl(movie.getId()))
                    .append("\n")
                    .append("\n");
            count++;
        }
        return builder.toString();
    }

    public String shortMovieDescription(Movie movie) {
        StringBuilder builder = new StringBuilder();

            builder.append(movie.getName());

            if (movie.getAlternativeName() != null) {
                builder.append(" / ")
                        .append(movie.getAlternativeName());
            }

            builder.append("\n")
                    .append(movie.getYear())
                    .append("\n")
                    .append(UrlBuilder.getKinopoiskUrl(movie.getId()))
                    .append("\n")
                    .append("\n");

        return builder.toString();
    }


}
