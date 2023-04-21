package com.smileksey.movie2watch.botapi.handlers;

import com.smileksey.movie2watch.KinopoiskApi;
import com.smileksey.movie2watch.ReplyUtil;
import com.smileksey.movie2watch.botapi.BotState;
import com.smileksey.movie2watch.botapi.MainMenuService;
import com.smileksey.movie2watch.cache.UserChoiceData;
import com.smileksey.movie2watch.cache.UserDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class FillingMovieParamsHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private final ReplyUtil replyUtil;
    private final KinopoiskApi kinopoiskApi;
    private final String initialUrl = "https://api.kinopoisk.dev/v1/movie?selectFields=id name alternativeName year description poster countries genres videos rating&limit=1&name=!null&description=!null";
    private final MainMenuService mainMenuService;
    private StringBuilder urlBuilder = new StringBuilder(initialUrl);
    @Autowired
    public FillingMovieParamsHandler(UserDataCache userDataCache, ReplyUtil replyUtil, KinopoiskApi kinopoiskApi, MainMenuService mainMenuService) {
        this.userDataCache = userDataCache;
        this.replyUtil = replyUtil;
        this.kinopoiskApi = kinopoiskApi;
        this.mainMenuService = mainMenuService;
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

        if (botState.equals(BotState.WAIT_FOR_GENRE)) {
            replyMessage.setText("Выбери жанр (или введи свой)");
            replyMessage.setReplyMarkup(getGenreKeyboard());
            //
            System.out.println(urlBuilder);

            userDataCache.setUsersCurrentBotState(userId, BotState.WAIT_FOR_YEAR);
        }

        if (botState.equals(BotState.WAIT_FOR_YEAR)) {
            choiceData.setGenre(usersAnswer);
            urlBuilder.append("&genres.name=").append(usersAnswer);

            replyMessage.setText("Укажи год.\nМожно указать период (например: 2000-2010)");
            //
            System.out.println(urlBuilder);

            userDataCache.setUsersCurrentBotState(userId, BotState.WAIT_FOR_RATING);
        }

        if (botState.equals(BotState.WAIT_FOR_RATING)) {
            choiceData.setYear(usersAnswer);
            urlBuilder.append("&year=").append(usersAnswer);

            replyMessage.setText("Укажи минимальный рейтинг imdb:");
            //
            System.out.println(urlBuilder);

            userDataCache.setUsersCurrentBotState(userId, BotState.PARAMETERS_FILLED);
        }

        if (botState.equals(BotState.PARAMETERS_FILLED)) {
            choiceData.setRating(usersAnswer);
            urlBuilder.append("&rating.imdb=").append(usersAnswer).append("-10.0");
            //
            System.out.println(urlBuilder);

            //TODO убрать вывод фильма
            replyMessage.setText(replyUtil.movieDescription(kinopoiskApi.getRandomMovieByUrl(urlBuilder.toString())));

            replyMessage.setReplyMarkup(mainMenuService.getMainMenuKeyboard());

            //TODO url будет формироваться в отдельном методе для кнопки "Получить фильм по параметрам"
            urlBuilder = new StringBuilder(initialUrl);

            userDataCache.setUsersCurrentBotState(userId, BotState.DEFAULT);
        }

        userDataCache.saveUsersChoiceData(userId, choiceData);

        return replyMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PARAMETERS;
    }

    public ReplyKeyboardMarkup getGenreKeyboard() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        row1.add(new KeyboardButton("Ужасы"));
        row1.add(new KeyboardButton("Триллер"));
        row1.add(new KeyboardButton("Драма"));

        row2.add(new KeyboardButton("Комедия"));
        row2.add(new KeyboardButton("Боевик"));
        row2.add(new KeyboardButton("Фантастика"));

        keyboardRows.add(row1);
        keyboardRows.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }
}
