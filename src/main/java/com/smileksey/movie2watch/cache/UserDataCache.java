package com.smileksey.movie2watch.cache;

import com.smileksey.movie2watch.botapi.BotState;
import com.smileksey.movie2watch.models.UserChoiceData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/*Хранение текущего состояния бота и критериев выбора фильма для каждого пользователя*/

@Component
public class UserDataCache implements DataCache{
    private Map<Long, BotState> usersBotStates = new HashMap<>();

    //Не используется - данные хранятся в БД
    private Map<Long, UserChoiceData> usersChoiceData = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(long userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(long userId) {
        BotState botState = usersBotStates.get(userId);

        if (botState == null) {
            botState = BotState.DEFAULT;
        }
        return botState;
    }

    //Не используется - данные хранятся в БД
    @Override
    public UserChoiceData getUsersChoiceData(long userId) {
        UserChoiceData userChoiceData = usersChoiceData.get(userId);

        if (userChoiceData == null) {
            userChoiceData = new UserChoiceData();
        }
        return userChoiceData;
    }

    //Не используется - данные хранятся в БД
    @Override
    public void saveUsersChoiceData(long userId, UserChoiceData userChoiceData) {
        usersChoiceData.put(userId, userChoiceData);
    }
}
