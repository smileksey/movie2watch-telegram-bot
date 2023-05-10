package com.smileksey.movie2watch.botapi.handlers;

import com.smileksey.movie2watch.Bot;
import com.smileksey.movie2watch.KinopoiskApi;
import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.models.UserChoiceData;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import com.smileksey.movie2watch.services.TgUserService;
import com.smileksey.movie2watch.services.UserChoiceDataService;
import com.smileksey.movie2watch.util.Keyboards;
import com.smileksey.movie2watch.util.ReplyUtil;
import com.smileksey.movie2watch.util.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
public class ScheduledMessagesSender {
    
    private final TgUserService tgUserService;
    private final UserChoiceDataService userChoiceDataService;
    private final ReplyUtil replyUtil;
    private final Bot bot;
    private final KinopoiskApi kinopoiskApi;

    @Autowired
    public ScheduledMessagesSender(TgUserService tgUserService, UserChoiceDataService userChoiceDataService, 
                                   ReplyUtil replyUtil, @Lazy Bot bot, KinopoiskApi kinopoiskApi) {
        this.tgUserService = tgUserService;
        this.userChoiceDataService = userChoiceDataService;
        this.replyUtil = replyUtil;
        this.bot = bot;
        this.kinopoiskApi = kinopoiskApi;
    }


    @Scheduled(cron = "0 0 17 * * 5,6")
    private void sendMovieCompilation() {
        List<TgUser> subscribedUsers = tgUserService.getSubscribedUsers();

        subscribedUsers.forEach(tgUser -> {
            UserChoiceData userChoiceData = userChoiceDataService.getUserChoiceData(tgUser).orElse(null);
            SendMessage movieReply;
            SendMessage helloMessage;

            if (userChoiceData != null && userChoiceData.getRating() != null) {

                helloMessage = replyUtil.textReply(tgUser.getChatId(), "Подборка фильмов специально для вас:");
                bot.executeMessage(helloMessage);

                for (int i = 0; i < 3; i++) {

                    Movie movie = kinopoiskApi.getRandomMovieByUrl(UrlBuilder.buildUrl(userChoiceData.getGenre(),
                            userChoiceData.getYear(), userChoiceData.getRating()));

                    if (movie != null) {
                        movieReply = replyUtil.textReply(tgUser.getChatId(), replyUtil.movieDescription(movie));
                        movieReply.setReplyMarkup(Keyboards.getBottomLineButtons("/customRandom", movie));
                        bot.executeMessage(movieReply);
                    }
                }
            } else {
                helloMessage = replyUtil.textReply(tgUser.getChatId(), "Похоже, ваших предпочтений нет в базе...\n" +
                        " Укажите их с помощью /preferences.");
                bot.executeMessage(helloMessage);
            }
        });

    }
}
