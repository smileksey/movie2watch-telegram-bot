package com.smileksey.movie2watch.botapi;

import com.smileksey.movie2watch.botapi.handlers.DefaultMessageHandler;
import com.smileksey.movie2watch.botapi.handlers.FillingMovieParamsHandler;
import com.smileksey.movie2watch.botapi.handlers.InputMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class BotStateContext {

    private final DefaultMessageHandler defaultMessageHandler;
    private final FillingMovieParamsHandler movieParamsHandler;

    public BotStateContext(DefaultMessageHandler defaultMessageHandler, FillingMovieParamsHandler movieParamsHandler) {
        this.defaultMessageHandler = defaultMessageHandler;
        this.movieParamsHandler = movieParamsHandler;
    }

    public SendMessage processInputMessage(BotState botState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(botState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState botState) {
        if (isFillingParametersState(botState)) {
            return movieParamsHandler;
        }
        return defaultMessageHandler;
    }

    private boolean isFillingParametersState(BotState botState) {
        switch (botState) {
            case FILLING_PARAMETERS:
            case WAIT_FOR_GENRE:
            case WAIT_FOR_YEAR:
            case WAIT_FOR_RATING:
            case PARAMETERS_FILLED:
                return true;
            default:
                return false;
        }
    }
}
