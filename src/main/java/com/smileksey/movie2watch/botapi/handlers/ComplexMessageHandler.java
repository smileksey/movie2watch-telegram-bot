package com.smileksey.movie2watch.botapi.handlers;

import com.smileksey.movie2watch.Bot;
import com.smileksey.movie2watch.KinopoiskApi;
import com.smileksey.movie2watch.botapi.BotState;
import com.smileksey.movie2watch.cache.MovieCache;
import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import com.smileksey.movie2watch.services.MovieService;
import com.smileksey.movie2watch.services.TgUserService;
import com.smileksey.movie2watch.util.Keyboards;
import com.smileksey.movie2watch.util.ReplyUtil;
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
                movieService.deleteMovieById(Integer.parseInt(command[1]));
                replyMessage = replyUtil.textReply(message.getChatId(), "Фильм удален");
                break;

            case "/save":
                tgUser = tgUserService.getOrCreateUserFromMessage(message);
                movie = kinopoiskApi.getMovieById(Integer.parseInt(command[1]));

                if (movie != null) {
                    movieService.save(movie, tgUser);
                }

                replyMessage = replyUtil.textReply(chatId, "Фильм сохранен!");
                break;

            case "Подобрать":
                if (command[1].equals("фильм")) {
                    replyMessage = replyUtil.textReply(chatId, "Выбери один из вариантов:");
                    replyMessage.setReplyMarkup(Keyboards.getSearchTypeButtons());
                }
                break;

            case "/markaswatched":
                movie = movieService.getMovieById(Integer.parseInt(command[1]));

                if (movie != null) {
                    try {
                        tgUser = tgUserService.getOrCreateUserFromMessage(message);
                        movieService.changeWatchedStatus(movie, true, tgUser);
                        replyMessage = replyUtil.textReply(chatId, "Фильм отмечен как просмотренный");
                    } catch (Exception e) {
                        replyMessage = replyUtil.textReply(chatId, "Не удалось применить Изменения. Попробуйте еще раз.");
                    }
                } else {
                    replyMessage = replyUtil.textReply(chatId, "Произошла ошибка. Попробуйте еще раз.");
                }
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
}
