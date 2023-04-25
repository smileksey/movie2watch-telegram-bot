package com.smileksey.movie2watch.util;

import com.smileksey.movie2watch.models.Movie;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboards {

    public static ReplyKeyboardMarkup getMainMenuKeyboard() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();

        row1.add(new KeyboardButton("Подобрать фильм"));

        keyboardRows.add(row1);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getSearchTypeButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton random = new InlineKeyboardButton();
        random.setText("Случайный фильм");
        random.setCallbackData("/random");

        InlineKeyboardButton randomHorror = new InlineKeyboardButton();
        randomHorror.setText("Случайный фильм ужасов");
        randomHorror.setCallbackData("/randomhorror");

        InlineKeyboardButton customRandom = new InlineKeyboardButton();
        customRandom.setText("Случайный фильм по указанным предпочтениям");
        customRandom.setCallbackData("/customrandom");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(random);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(randomHorror);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(customRandom);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getReSearchButton(String command, Movie movie) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton reSearchButton = new InlineKeyboardButton();
        reSearchButton.setText("Повторить поиск");
        reSearchButton.setCallbackData(command);

        InlineKeyboardButton kpUrlButton = new InlineKeyboardButton();
        kpUrlButton.setText("Страница на Кинопоиске");
        kpUrlButton.setUrl(UrlBuilder.getKinopoiskUrl(movie.getId()));

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(kpUrlButton);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(reSearchButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getGenreButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton horror = new InlineKeyboardButton();
        horror.setText("Ужасы");
        horror.setCallbackData("ужасы");

        InlineKeyboardButton thriller = new InlineKeyboardButton();
        thriller.setText("Триллер");
        thriller.setCallbackData("триллер");

        InlineKeyboardButton drama = new InlineKeyboardButton();
        drama.setText("Драма");
        drama.setCallbackData("драма");

        InlineKeyboardButton comedy = new InlineKeyboardButton();
        comedy.setText("Комедия");
        comedy.setCallbackData("комедия");

        InlineKeyboardButton action = new InlineKeyboardButton();
        action.setText("Боевик");
        action.setCallbackData("боевик");

        InlineKeyboardButton fiction = new InlineKeyboardButton();
        fiction.setText("Фантистика");
        fiction.setCallbackData("фантастика");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(horror);
        keyboardButtonsRow1.add(thriller);
        keyboardButtonsRow1.add(drama);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(comedy);
        keyboardButtonsRow2.add(action);
        keyboardButtonsRow2.add(fiction);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getGenreKeyboard() {

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
