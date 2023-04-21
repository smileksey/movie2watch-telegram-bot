package com.smileksey.movie2watch;

import com.smileksey.movie2watch.botapi.TelegramFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@PropertySource("classpath:application.properties")
public class Bot extends TelegramLongPollingBot {

    private final TelegramFacade telegramFacade;

    @Value("${bot.name}")
    private String name;

    @Autowired
    public Bot(@Value("${bot.token}") String botToken, TelegramFacade telegramFacade) {
        super(botToken);
        this.telegramFacade = telegramFacade;
    }

    @Override
    public void onUpdateReceived(Update update) {

        try {
            execute(telegramFacade.handleUpdate(update));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

}
