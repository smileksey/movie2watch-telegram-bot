package com.smileksey.movie2watch.cache;

import com.smileksey.movie2watch.botapi.BotState;
import com.smileksey.movie2watch.models.UserChoiceData;


public interface DataCache {
    void setUsersCurrentBotState(long userId, BotState botState);

    BotState getUsersCurrentBotState(long userId);

    UserChoiceData getUsersChoiceData(long userId);

    void saveUsersChoiceData(long userId, UserChoiceData userChoiceData);
}
