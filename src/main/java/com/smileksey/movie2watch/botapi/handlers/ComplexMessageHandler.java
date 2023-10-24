package com.smileksey.movie2watch.botapi.handlers;

import com.smileksey.movie2watch.Bot;
import com.smileksey.movie2watch.KinopoiskApi;
import com.smileksey.movie2watch.botapi.BotState;
import com.smileksey.movie2watch.cache.MovieCache;
import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.models.TgUserMovie;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import com.smileksey.movie2watch.services.MovieService;
import com.smileksey.movie2watch.services.TgUserService;
import com.smileksey.movie2watch.util.Keyboards;
import com.smileksey.movie2watch.util.ReplyUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class ComplexMessageHandler implements InputMessageHandler {

    private final ReplyUtil replyUtil;
    private final MovieService movieService;
    private final MovieCache movieCache;
    private final KinopoiskApi kinopoiskApi;
    private final Bot bot;
    private final TgUserService tgUserService;
    private final static Logger logger = LogManager.getLogger(ComplexMessageHandler.class);

    @Autowired
    public ComplexMessageHandler(ReplyUtil replyUtil, MovieService movieService, MovieCache movieCache, KinopoiskApi kinopoiskApi, @Lazy Bot bot, TgUserService tgUserService) {
        this.replyUtil = replyUtil;
        this.movieService = movieService;
        this.movieCache = movieCache;
        this.kinopoiskApi = kinopoiskApi;
        this.bot = bot;
        this.tgUserService = tgUserService;
    }

    @Override
    public SendMessage handle(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage replyMessage = null;
        String[] command = message.getText().split(" ");
        Movie movie;
        TgUser tgUser;

        switch (command[0]) {
            case "/delete":
                movieService.deleteMovieById(userId, Integer.parseInt(command[1]));
                movie = movieService.getMovieById(Integer.parseInt(command[1]));

                replyMessage = replyUtil.textReply(message.getChatId(), "Фильм удален");

                logger.info("User {} deleted movie {} from Favourites", message.getFrom().getUserName(), movie.getName());
                break;

            case "/save":
                tgUser = tgUserService.getOrCreateUserFromMessage(message);
                movie = kinopoiskApi.getMovieById(Integer.parseInt(command[1]));

                if (movie != null) {
                    movieService.save(movie, tgUser);
                    replyMessage = replyUtil.textReply(chatId, "Фильм сохранен!");
                    logger.info("User {} saved movie {} to Favourites", message.getFrom().getUserName(), movie.getName());
                } else {
                    replyMessage = replyUtil.textReply(chatId, "Ошибка... Не удалось сохранить");
                    logger.error("User {} tried to save a movie to Favourites. An error occurred", message.getFrom().getUserName());
                }

                break;

            case "Подобрать":
                if (command[1].equals("фильм")) {
                    replyMessage = replyUtil.textReply(chatId, "Выбери один из вариантов:");
                    replyMessage.setReplyMarkup(Keyboards.getSearchTypeButtons());
                    logger.info("User {} pressed 'Подобрать фильм", message.getFrom().getUserName());
                }
                break;

            case "/markaswatched":
                int movieId = Integer.parseInt(command[1]);
                movie = movieService.getMovieById(Integer.parseInt(command[1]));
                TgUserMovie userMovie = movieService.getUserMovie(userId, movieId);

                if (userMovie != null) {
                    try {
                        movieService.changeWatchedStatus(userMovie, true);
                        replyMessage = replyUtil.textReply(chatId, "Фильм отмечен как просмотренный");
                        logger.info("User {} marked movie {} as watched", message.getFrom().getUserName(), movie.getName());
                    } catch (Exception e) {
                        replyMessage = replyUtil.textReply(chatId, "Не удалось применить Изменения. Попробуйте еще раз.");
                        logger.error("User {} tried to mark movie {} as watched. An error occurred", message.getFrom().getUserName(), movie.getName());
                    }
                } else {
                    replyMessage = replyUtil.textReply(chatId, "Произошла ошибка. Попробуйте еще раз.");
                    logger.error("User {} tried to mark a movie as watched. An error occurred", message.getFrom().getUserName());
                }
                break;

            default:
                replyMessage = replyUtil.textReply(chatId, "Извините, я не знаю такой команды...");
                logger.info("User {} entered unknown command: '{}'", message.getFrom().getUserName(), message.getText());
                break;

        }
        return replyMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.DEFAULT;
    }
}
