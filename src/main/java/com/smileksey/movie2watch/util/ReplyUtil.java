package com.smileksey.movie2watch.util;

import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@PropertySource("classpath:application.properties")
public class ReplyUtil {
    @Value("${log.directory}")
    private String logDirectory;

    public SendMessage startReply(long chatId) {
        String reply = "Привет Чтобы начать, нажми '<b>Подобрать фильм</b>' или воспользуйся <b>меню</b>.\n" +
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

            builder
                    .append("<b><u>")
                    .append(movie.getName())
                    .append("</u></b>");

            if (movie.getAlternativeName() != null) {
                builder
                        .append(" / ")
                        .append("<b><u>")
                        .append(movie.getAlternativeName())
                        .append("</u></b>");
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
                        .append("Рейтинг IMDb: ")
                        .append("<b>")
                        .append(movie.getRating().getImdb())
                        .append("</b>")
                        .append(" / КП: ")
                        .append("<b>")
                        .append(movie.getRating().getKp())
                        .append("</b>")
                        .append("\n");
            }

            builder
                    .append("\n")
                    .append("<i>")
                    .append(movie.getDescription())
                    .append("</i>")
                    .append("\n");

            if (movie.getPoster() != null && movie.getPoster().getUrl() != null) {
                builder.append("<a href=\"" + movie.getPoster().getUrl() + "\"> </a>");
            }

            if (movie.getVideos() != null && !movie.getVideos().getTrailers().isEmpty()) {

                builder
                        .append("\n")
                        .append("<a href=\"" + movie.getVideos().getTrailers().get(0).getUrl() + "\">Трейлер</a>");
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

            builder
                    .append("<b><u>")
                    .append(movie.getName())
                    .append("</u></b>");

            if (movie.getAlternativeName() != null) {
                builder
                        .append(" / ")
                        .append("<b><u>")
                        .append(movie.getAlternativeName())
                        .append("</u></b>");
            }

            builder.append("\n")
                    .append(movie.getYear())
                    .append("\n")
                    .append(UrlBuilder.getKinopoiskUrl(movie.getId()))
                    .append("\n")
                    .append("\n");

        return builder.toString();
    }

    public File getLastLog() {
        File directory = new File(logDirectory);

        if (directory.isDirectory()) {
            File[] logs = directory.listFiles(file -> file.isFile());
            if (logs != null) {
                Optional<File> opLog = Arrays.stream(logs).max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
                return opLog.orElse(null);
            }
        }
        return null;
    }



}
