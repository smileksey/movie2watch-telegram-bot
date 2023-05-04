package com.smileksey.movie2watch.botapi.handlers;

import com.smileksey.movie2watch.botapi.TelegramFacade;
import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.services.TgUserService;
import com.smileksey.movie2watch.services.UserChoiceDataService;
import com.smileksey.movie2watch.util.Keyboards;
import com.smileksey.movie2watch.botapi.BotState;
import com.smileksey.movie2watch.models.UserChoiceData;
import com.smileksey.movie2watch.cache.UserDataCache;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class FillingMovieParamsHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final TelegramFacade telegramFacade;
    private final UserChoiceDataService userChoiceDataService;
    private final TgUserService tgUserService;

    @Autowired
    public FillingMovieParamsHandler(UserDataCache userDataCache, @Lazy TelegramFacade telegramFacade,
                                     UserChoiceDataService userChoiceDataService, TgUserService tgUserService) {
        this.userDataCache = userDataCache;
        this.telegramFacade = telegramFacade;
        this.userChoiceDataService = userChoiceDataService;
        this.tgUserService = tgUserService;
    }

    @Override
    public SendMessage handle(Message message) {

        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PARAMETERS)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.WAIT_FOR_GENRE);
        }

        return processUsersInput(message);
    }

    @Transactional
    private SendMessage processUsersInput(Message message) {
        String usersAnswer = message.getText().toLowerCase().trim();
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();

        //Если пользователь и его предпочтения уже есть в БД - получаем, если нет - создаем новую запись в БД
        TgUser tgUser = tgUserService.getOrCreateUserFromMessage(message);
        UserChoiceData choiceData = userChoiceDataService.getOrCreateUserChoiceData(tgUser);

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

            replyMessage.setText("Укажи год.\nМожно указать период в формате 'yyyy-yyyy'\n(например 2000-2010)");

            userDataCache.setUsersCurrentBotState(userId, BotState.WAIT_FOR_RATING);
        }

        if (botState.equals(BotState.WAIT_FOR_RATING)) {

            choiceData.setYear(usersAnswer);

            replyMessage.setText("Укажи минимальный рейтинг imdb (например 6 или 5.5):");

            userDataCache.setUsersCurrentBotState(userId, BotState.PARAMETERS_FILLED);
        }

        if (botState.equals(BotState.PARAMETERS_FILLED)) {

            choiceData.setRating(usersAnswer);

            replyMessage.setText("Предпочтения сохранены!");

            replyMessage.setReplyMarkup(Keyboards.getMainMenuKeyboard());

            userDataCache.setUsersCurrentBotState(userId, BotState.DEFAULT);
        }

        //сохраняем предпочтения пользователя в БД
        userChoiceDataService.save(choiceData);

        return replyMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PARAMETERS;
    }






}
