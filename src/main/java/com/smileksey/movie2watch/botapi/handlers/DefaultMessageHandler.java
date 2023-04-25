package com.smileksey.movie2watch.botapi.handlers;

import com.smileksey.movie2watch.KinopoiskApi;
import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.services.UserChoiceDataService;
import com.smileksey.movie2watch.util.Keyboards;
import com.smileksey.movie2watch.util.ReplyUtil;
import com.smileksey.movie2watch.botapi.BotState;
import com.smileksey.movie2watch.models.UserChoiceData;
import com.smileksey.movie2watch.cache.UserDataCache;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import com.smileksey.movie2watch.util.UrlBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class DefaultMessageHandler implements InputMessageHandler{

    private final ReplyUtil replyUtil;
    private final KinopoiskApi kinopoiskApi;
    private final UserDataCache userDataCache;
    private final UserChoiceDataService userChoiceDataService;

    public DefaultMessageHandler(ReplyUtil replyUtil, KinopoiskApi kinopoiskApi, UserDataCache userDataCache, UserChoiceDataService userChoiceDataService) {
        this.replyUtil = replyUtil;
        this.kinopoiskApi = kinopoiskApi;
        this.userDataCache = userDataCache;
        this.userChoiceDataService = userChoiceDataService;
    }

    @Override
    public SendMessage handle(Message message) {
        long userId = message.getFrom().getId();
        SendMessage replyMessage = null;

        switch (message.getText()) {
            case "/start":
                replyMessage = replyUtil.startReply(message.getChatId());
                break;
            case "Подобрать фильм":
                replyMessage = replyUtil.textReply(message.getChatId(), "Выбери один из вариантов:");
                replyMessage.setReplyMarkup(Keyboards.getSearchTypeButtons());
                break;
            case "/random":
                Movie randomMovie = kinopoiskApi.getRandomMovie();
                replyMessage = replyUtil.textReply(message.getChatId(), replyUtil.movieDescription(randomMovie));
                replyMessage.setReplyMarkup(Keyboards.getReSearchButton(message.getText(), randomMovie));
                break;
            case "/randomhorror":
                Movie randomHorrorMovie = kinopoiskApi.getRandomHorrorMovie();
                replyMessage = replyUtil.textReply(message.getChatId(), replyUtil.movieDescription(randomHorrorMovie));
                replyMessage.setReplyMarkup(Keyboards.getReSearchButton(message.getText(), randomHorrorMovie));
                break;
            case "/genres":
                replyMessage = replyUtil.textReply(message.getChatId(), kinopoiskApi.getPossibleGenres());
                break;
            case "/customrandom":
                //UserChoiceData userChoiceData = userDataCache.getUsersChoiceData(userId);
                UserChoiceData userChoiceData = userChoiceDataService.get(new TgUser(userId)).orElse(null);

                if (userChoiceData != null && userChoiceData.getRating() != null) {
                    Movie movie = kinopoiskApi.getRandomMovieByUrl(UrlBuilder.buildUrl(userChoiceData.getGenre(), userChoiceData.getYear(), userChoiceData.getRating()));
                    if (movie != null) {
                        replyMessage = replyUtil.textReply(message.getChatId(), replyUtil.movieDescription(movie));
                        replyMessage.setReplyMarkup(Keyboards.getReSearchButton(message.getText(), movie));
                    } else {
                        replyMessage = replyUtil.textReply(message.getChatId(), "Произошла ошибка. Возможно, предпочтения заполнены некорректно. Попробуйте заполнить их снова и повторите попытку.");
                    }
                } else {
                    replyMessage = replyUtil.textReply(message.getChatId(), "Вы еще не указали предпочтения для поиска. Нажмите /preferences, чтобы это исправить.");
                }
                break;
            default:
                replyMessage = replyUtil.textReply(message.getChatId(), "Извините, я не знаю такой команды...");
                break;
        }

        return replyMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.DEFAULT;
    }

}
