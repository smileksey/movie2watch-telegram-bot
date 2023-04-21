package com.smileksey.movie2watch.botapi.handlers;

import com.smileksey.movie2watch.KinopoiskApi;
import com.smileksey.movie2watch.ReplyUtil;
import com.smileksey.movie2watch.botapi.BotState;
import com.smileksey.movie2watch.botapi.MainMenuService;
import com.smileksey.movie2watch.cache.UserChoiceData;
import com.smileksey.movie2watch.cache.UserDataCache;
import com.smileksey.movie2watch.models.Movie;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultMessageHandler implements InputMessageHandler{

    private final ReplyUtil replyUtil;
    private final KinopoiskApi kinopoiskApi;
    private final MainMenuService mainMenuService;
    private final UserDataCache userDataCache;

    public DefaultMessageHandler(ReplyUtil replyUtil, KinopoiskApi kinopoiskApi, MainMenuService mainMenuService, UserDataCache userDataCache) {
        this.replyUtil = replyUtil;
        this.kinopoiskApi = kinopoiskApi;
        this.mainMenuService = mainMenuService;
        this.userDataCache = userDataCache;
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
                replyMessage.setReplyMarkup(getInlineMessageButtons());
                break;
            case "/random":
                Movie randomMovie = kinopoiskApi.getRandomMovie();
                replyMessage = replyUtil.textReply(message.getChatId(), replyUtil.movieDescription(randomMovie));
                break;
            case "/randomhorror":
                Movie randomHorrorMovie = kinopoiskApi.getRandomHorrorMovie();
                replyMessage = replyUtil.textReply(message.getChatId(), replyUtil.movieDescription(randomHorrorMovie));
                break;
            case "/genres":
                replyMessage = replyUtil.textReply(message.getChatId(), kinopoiskApi.getPossibleGenres());
                break;
            case "Повторить поиск по указанным критериям":
                UserChoiceData userChoiceData = userDataCache.getUsersChoiceData(userId);

                if (userChoiceData.getRating() != null) {
                    replyMessage = replyUtil.textReply(message.getChatId(), replyUtil.movieDescription(kinopoiskApi.getRandomMovieByUrl(urlBuilder(userChoiceData.getGenre(), userChoiceData.getYear(), userChoiceData.getRating()))));
                } else {
                    replyMessage = replyUtil.textReply(message.getChatId(), "Вы еще не вводили критерии для поиска. Нажмите 'Подобрать фильм' -> 'Случайный фильм по критериям'");
                }
                break;
            default:
                replyMessage = replyUtil.textReply(message.getChatId(), "Извини, я не знаю такой команды...");
                break;
        }

        return replyMessage;
    }

    private String urlBuilder(String genre, String year, String rating) {
        StringBuilder url = new StringBuilder("https://api.kinopoisk.dev/v1/movie?selectFields=id name alternativeName year description poster countries genres videos rating&limit=1&name=!null&description=!null");
        url.append("&genres.name=")
                .append(genre)
                .append("&year=")
                .append(year)
                .append("&rating.imdb=")
                .append(rating)
                .append("-10.0");
        return url.toString();
    }

    @Override
    public BotState getHandlerName() {
        return BotState.DEFAULT;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton randomButton = new InlineKeyboardButton();
        InlineKeyboardButton randomHorrorButton = new InlineKeyboardButton();
        InlineKeyboardButton customRandomButton = new InlineKeyboardButton();

        randomButton.setText("Случайный фильм");
        randomHorrorButton.setText("Случайный фильм ужасов");
        customRandomButton.setText("Случайный фильм по критериям");

        randomButton.setCallbackData("random");
        randomHorrorButton.setCallbackData("randomHorror");
        customRandomButton.setCallbackData("customRandom");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(randomButton);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(randomHorrorButton);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(customRandomButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }


}
