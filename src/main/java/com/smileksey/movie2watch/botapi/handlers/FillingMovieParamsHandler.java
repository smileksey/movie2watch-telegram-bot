package com.smileksey.movie2watch.botapi.handlers;

import com.smileksey.movie2watch.KinopoiskApi;
import com.smileksey.movie2watch.botapi.TelegramFacade;
import com.smileksey.movie2watch.util.Keyboards;
import com.smileksey.movie2watch.util.ReplyUtil;
import com.smileksey.movie2watch.botapi.BotState;
import com.smileksey.movie2watch.cache.UserChoiceData;
import com.smileksey.movie2watch.cache.UserDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class FillingMovieParamsHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private final ReplyUtil replyUtil;
    private final KinopoiskApi kinopoiskApi;
    private final TelegramFacade telegramFacade;

    @Autowired
    public FillingMovieParamsHandler(UserDataCache userDataCache, ReplyUtil replyUtil, KinopoiskApi kinopoiskApi, @Lazy TelegramFacade telegramFacade) {
        this.userDataCache = userDataCache;
        this.replyUtil = replyUtil;
        this.kinopoiskApi = kinopoiskApi;
        this.telegramFacade = telegramFacade;
    }

    @Override
    public SendMessage handle(Message message) {

        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PARAMETERS)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.WAIT_FOR_GENRE);
        }
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message message) {
        String usersAnswer = message.getText().toLowerCase().trim();
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();

        //TODO будет доставаться из БД, а не из памяти
        UserChoiceData choiceData = userDataCache.getUsersChoiceData(userId);

        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(chatId);

        if (usersAnswer.startsWith("/") && !usersAnswer.equals("/preferences")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.DEFAULT);
            return telegramFacade.handleInputMessage(message);
        }


        if (botState.equals(BotState.WAIT_FOR_GENRE)) {
            replyMessage.setText("Выбери жанр (или введи свой)");
            replyMessage.setReplyMarkup(Keyboards.getGenreButtons());

            userDataCache.setUsersCurrentBotState(userId, BotState.WAIT_FOR_YEAR);
        }

        if (botState.equals(BotState.WAIT_FOR_YEAR)) {

            choiceData.setGenre(usersAnswer);
            replyMessage.setText("Укажи год.\nМожно указать период (например: 2000-2010)");

            userDataCache.setUsersCurrentBotState(userId, BotState.WAIT_FOR_RATING);

        }

        if (botState.equals(BotState.WAIT_FOR_RATING)) {
            choiceData.setYear(usersAnswer);

            replyMessage.setText("Укажи минимальный рейтинг imdb:");

            userDataCache.setUsersCurrentBotState(userId, BotState.PARAMETERS_FILLED);
        }

        if (botState.equals(BotState.PARAMETERS_FILLED)) {
            choiceData.setRating(usersAnswer);

            replyMessage.setText("Предпочтения сохранены!");

            replyMessage.setReplyMarkup(Keyboards.getMainMenuKeyboard());

            userDataCache.setUsersCurrentBotState(userId, BotState.DEFAULT);
        }

        userDataCache.saveUsersChoiceData(userId, choiceData);

        return replyMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PARAMETERS;
    }






}
