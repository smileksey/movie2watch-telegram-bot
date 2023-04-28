package com.smileksey.movie2watch.botapi.handlers;

import com.smileksey.movie2watch.Bot;
import com.smileksey.movie2watch.KinopoiskApi;
import com.smileksey.movie2watch.cache.MovieCache;
import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.services.MovieService;
import com.smileksey.movie2watch.services.UserChoiceDataService;
import com.smileksey.movie2watch.util.Keyboards;
import com.smileksey.movie2watch.util.ReplyUtil;
import com.smileksey.movie2watch.botapi.BotState;
import com.smileksey.movie2watch.models.UserChoiceData;
import com.smileksey.movie2watch.cache.UserDataCache;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import com.smileksey.movie2watch.util.UrlBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class DefaultMessageHandler implements InputMessageHandler{

    private final ReplyUtil replyUtil;
    private final KinopoiskApi kinopoiskApi;
    private final UserDataCache userDataCache;
    private final UserChoiceDataService userChoiceDataService;
    private final MovieService movieService;
    private final Bot bot;
    private final MovieCache movieCache;

    public DefaultMessageHandler(ReplyUtil replyUtil, KinopoiskApi kinopoiskApi, UserDataCache userDataCache, UserChoiceDataService userChoiceDataService, MovieService movieService, @Lazy Bot bot, MovieCache movieCache) {
        this.replyUtil = replyUtil;
        this.kinopoiskApi = kinopoiskApi;
        this.userDataCache = userDataCache;
        this.userChoiceDataService = userChoiceDataService;
        this.movieService = movieService;
        this.bot = bot;
        this.movieCache = movieCache;
    }

    @Override
    public SendMessage handle(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage replyMessage = null;
        Movie movie;

        switch (message.getText()) {
            case "/start":
                replyMessage = replyUtil.startReply(message.getChatId());
                break;

            case "/help":
                replyMessage = replyUtil.helpReply(message.getChatId());
                break;

            case "/random":
                movie = kinopoiskApi.getRandomMovie();
                //movieCache.putMovie(randomMovie.getId(), randomMovie);

                replyMessage = replyUtil.textReply(chatId, replyUtil.movieDescription(movie));
                replyMessage.setReplyMarkup(Keyboards.getBottomLineButtons(message.getText(), movie));
                break;

            case "/randomhorror":
                movie = kinopoiskApi.getRandomHorrorMovie();
                //movieCache.putMovie(randomHorrorMovie.getId(), randomHorrorMovie);

                replyMessage = replyUtil.textReply(chatId, replyUtil.movieDescription(movie));
                replyMessage.setReplyMarkup(Keyboards.getBottomLineButtons(message.getText(), movie));
                break;

            case "/genres":
                replyMessage = replyUtil.textReply(chatId, kinopoiskApi.getPossibleGenres());
                break;

            case "/customrandom":

                //UserChoiceData userChoiceData = userDataCache.getUsersChoiceData(userId);
                UserChoiceData userChoiceData = userChoiceDataService.get(new TgUser(userId)).orElse(null);

                if (userChoiceData != null && userChoiceData.getRating() != null) {

                    movie = kinopoiskApi.getRandomMovieByUrl(UrlBuilder.buildUrl(userChoiceData.getGenre(), userChoiceData.getYear(), userChoiceData.getRating()));

                    if (movie != null) {
                        replyMessage = replyUtil.textReply(chatId, replyUtil.movieDescription(movie));
                        replyMessage.setReplyMarkup(Keyboards.getBottomLineButtons(message.getText(), movie));
                        //movieCache.putMovie(movie.getId(), movie);
                    } else {
                        replyMessage = replyUtil.textReply(chatId, "Произошла ошибка. Возможно, предпочтения заполнены некорректно. Попробуйте заполнить их снова (/preferences) и повторите попытку.");
                    }

                } else {
                    replyMessage = replyUtil.textReply(chatId, "Вы еще не указали предпочтения для поиска. Нажмите /preferences, чтобы это исправить.");
                }
                break;

            case "/watchlater":
                replyMessage = sendFavoriteMovies(chatId, userId, false);
                break;

            case "/watched":
                replyMessage = sendFavoriteMovies(chatId, userId, true);
                break;

            default:
                replyMessage = replyUtil.textReply(chatId, "Извините, я не знаю такой команды...");
                break;

        }
        return replyMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.DEFAULT;
    }

    private SendMessage sendFavoriteMovies(long chatId, long userId, boolean isWatched) {

        SendMessage replyMessage;
        String reply = "";

        if (isWatched) {
            reply = "Ваш список просмотренных фильмов";
        } else {
            reply = "Ваш список фильмов к просмотру";
        }

        List<Movie> movies = movieService.getMoviesAddedByUserIsWatched(new TgUser(userId), isWatched);

        if (!movies.isEmpty()) {
            replyMessage = replyUtil.textReply(chatId, reply);

            movies.forEach(movie1 -> {
                try {
                    SendMessage sendMessage = replyUtil.textReply(chatId, replyUtil.shortMovieDescription(movie1));
                    if (isWatched) {
                        sendMessage.setReplyMarkup(Keyboards.getWatchedMoviesButtons(movie1));
                    } else {
                        sendMessage.setReplyMarkup(Keyboards.getUnwatchedMoviesButtons(movie1));
                    }
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });

        } else {
            replyMessage = replyUtil.textReply(chatId, "Вы пока не добавили ни одного фильма");
        }
        return replyMessage;
    }

}
