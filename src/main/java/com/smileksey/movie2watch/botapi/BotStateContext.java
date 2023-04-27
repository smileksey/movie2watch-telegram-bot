package com.smileksey.movie2watch.botapi;

import com.smileksey.movie2watch.botapi.handlers.ComplexMessageHandler;
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
    private final ComplexMessageHandler complexMessageHandler;

    public BotStateContext(DefaultMessageHandler defaultMessageHandler, FillingMovieParamsHandler movieParamsHandler, ComplexMessageHandler complexMessageHandler) {
        this.defaultMessageHandler = defaultMessageHandler;
        this.movieParamsHandler = movieParamsHandler;
        this.complexMessageHandler = complexMessageHandler;
    }

    public SendMessage processInputMessage(BotState botState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(botState, message);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState botState, Message message) {
        if (isFillingParametersState(botState)) {
            return movieParamsHandler;
        }
        else if (message.getText().split(" ").length > 1) {
            return complexMessageHandler;
        }
        else return defaultMessageHandler;
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
