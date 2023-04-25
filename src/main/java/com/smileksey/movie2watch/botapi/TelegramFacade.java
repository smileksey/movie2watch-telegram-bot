package com.smileksey.movie2watch.botapi;

import com.smileksey.movie2watch.KinopoiskApi;
import com.smileksey.movie2watch.util.Keyboards;
import com.smileksey.movie2watch.util.ReplyUtil;
import com.smileksey.movie2watch.cache.UserDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;

@Component
public class TelegramFacade {
    private final UserDataCache userDataCache;
    private final BotStateContext botStateContext;
    private final ReplyUtil replyUtil;
    private final KinopoiskApi kinopoiskApi;

    @Autowired
    public TelegramFacade(UserDataCache userDataCache, BotStateContext botStateContext, ReplyUtil replyUtil, KinopoiskApi kinopoiskApi) {
        this.userDataCache = userDataCache;
        this.botStateContext = botStateContext;
        this.replyUtil = replyUtil;
        this.kinopoiskApi = kinopoiskApi;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return processCallBackQuery(callbackQuery);
        }

        Message inputMessage = update.getMessage();

        if (inputMessage != null && inputMessage.hasText()) {
            replyMessage = handleInputMessage(inputMessage);
        }

        return replyMessage;
    }

    public SendMessage handleInputMessage(Message inputMessage) {
        String messageText = inputMessage.getText();
        long userId = inputMessage.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (messageText) {
            case "/preferences":
                botState = BotState.FILLING_PARAMETERS;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);
        replyMessage = botStateContext.processInputMessage(botState, inputMessage);

        if (botState == BotState.DEFAULT && replyMessage.getReplyMarkup() == null) {
            replyMessage.setReplyMarkup(Keyboards.getMainMenuKeyboard());
        }

        return replyMessage;
    }

    //TODO
    private BotApiMethod<?> processCallBackQuery(CallbackQuery callbackQuery) {

        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();
        BotApiMethod<?> callBackAnswer = null;

        if (callbackQuery.getData() != null) {
            callBackAnswer = handleInputMessage(createMessage(chatId, userId, callbackQuery.getData()));
        }

        return callBackAnswer;
    }

    private Message createMessage(long chatId, long userId, String text) {
        Message message = new Message();
        User user = new User();
        Chat chat = new Chat();

        user.setId(userId);
        chat.setId(chatId);

        message.setFrom(user);
        message.setChat(chat);
        message.setText(text);

        return message;
    }
}
