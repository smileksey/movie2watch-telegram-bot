package com.smileksey.movie2watch.botapi.handlers;

import com.smileksey.movie2watch.botapi.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface InputMessageHandler {
    SendMessage handle(Message message);

    BotState getHandlerName();


}
