package com.smileksey.movie2watch.botapi;

/*Возможные состояния бота*/
public enum BotState {
    DEFAULT,
    FILLING_PARAMETERS,
    WAIT_FOR_GENRE,
    WAIT_FOR_YEAR,
    WAIT_FOR_RATING,
    PARAMETERS_FILLED
}
