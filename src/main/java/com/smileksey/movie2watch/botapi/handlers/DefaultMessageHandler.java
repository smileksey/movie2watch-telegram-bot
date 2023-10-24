package com.smileksey.movie2watch.botapi.handlers;

import com.smileksey.movie2watch.Bot;
import com.smileksey.movie2watch.KinopoiskApi;
import com.smileksey.movie2watch.cache.MovieCache;
import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.services.MovieService;
import com.smileksey.movie2watch.services.TgUserService;
import com.smileksey.movie2watch.services.UserChoiceDataService;
import com.smileksey.movie2watch.util.Keyboards;
import com.smileksey.movie2watch.util.ReplyUtil;
import com.smileksey.movie2watch.botapi.BotState;
import com.smileksey.movie2watch.models.UserChoiceData;
import com.smileksey.movie2watch.cache.UserDataCache;
import com.smileksey.movie2watch.models.kinopoiskmodels.Movie;
import com.smileksey.movie2watch.util.UrlBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
@PropertySource("classpath:application.properties")
public class DefaultMessageHandler implements InputMessageHandler{

    private final ReplyUtil replyUtil;
    private final KinopoiskApi kinopoiskApi;
    private final UserDataCache userDataCache;
    private final UserChoiceDataService userChoiceDataService;
    private final MovieService movieService;
    private final TgUserService tgUserService;
    private final Bot bot;
    private final MovieCache movieCache;
    private final static Logger logger = LogManager.getLogger(DefaultMessageHandler.class);

    @Value("${admin.chatId}")
    long adminChatId;

    public DefaultMessageHandler(ReplyUtil replyUtil, KinopoiskApi kinopoiskApi, UserDataCache userDataCache, UserChoiceDataService userChoiceDataService, MovieService movieService, TgUserService tgUserService, @Lazy Bot bot, MovieCache movieCache) {
        this.replyUtil = replyUtil;
        this.kinopoiskApi = kinopoiskApi;
        this.userDataCache = userDataCache;
        this.userChoiceDataService = userChoiceDataService;
        this.movieService = movieService;
        this.tgUserService = tgUserService;
        this.bot = bot;
        this.movieCache = movieCache;
    }

    @Override
    public SendMessage handle(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage replyMessage = null;
        Movie movie;

        switch (message.getText()) {
            case "/start":
                logger.info("User {} pressed '/start'", message.getFrom().getUserName());
                replyMessage = replyUtil.startReply(message.getChatId());
                break;

            case "/help":
                logger.info("User {} pressed '/help'", message.getFrom().getUserName());
                replyMessage = replyUtil.helpReply(message.getChatId());
                break;

            case "/random":
                logger.info("User {} pressed '/random'", message.getFrom().getUserName());
                movie = kinopoiskApi.getRandomMovie();
                //movieCache.putMovie(randomMovie.getId(), randomMovie);

                replyMessage = replyUtil.textReply(chatId, replyUtil.movieDescription(movie));
                replyMessage.setReplyMarkup(Keyboards.getBottomLineButtons(message.getText(), movie));
                logger.info("Found movie '{}' with ID {} for user {}", movie.getName(), movie.getId(), message.getFrom().getUserName());
                break;

            case "/genres":
                replyMessage = replyUtil.textReply(chatId, kinopoiskApi.getPossibleGenres());
                logger.info("User {} pressed '/genres'", message.getFrom().getUserName());
                break;

            case "/customrandom":
                logger.info("User {} pressed '/customrandom'", message.getFrom().getUserName());
                //UserChoiceData userChoiceData = userDataCache.getUsersChoiceData(userId);
                UserChoiceData userChoiceData = userChoiceDataService.getUserChoiceData(new TgUser(userId)).orElse(null);

                if (userChoiceData != null && userChoiceData.getRating() != null) {

                    movie = kinopoiskApi.getRandomMovieByUrl(UrlBuilder.buildUrl(userChoiceData.getGenre(), userChoiceData.getYear(), userChoiceData.getRating()));

                    if (movie != null) {
                        replyMessage = replyUtil.textReply(chatId, replyUtil.movieDescription(movie));
                        replyMessage.setReplyMarkup(Keyboards.getBottomLineButtons(message.getText(), movie));
                        //movieCache.putMovie(movie.getId(), movie);
                        logger.info("Found movie '{}' with ID {} for user {}", movie.getName(), movie.getId(), message.getFrom().getUserName());
                    } else {
                        replyMessage = replyUtil.textReply(chatId, "Произошла ошибка. Возможно, предпочтения заполнены некорректно. Попробуйте заполнить их снова (/preferences) и повторите попытку.");
                        logger.error("Failed to find a movie for user {}", message.getFrom().getUserName());
                    }

                } else {
                    replyMessage = replyUtil.textReply(chatId, "Вы еще не указали предпочтения для поиска. Нажмите /preferences, чтобы это исправить.");
                    logger.info("User {} has not specified preferences yet, movie NOT found", message.getFrom().getUserName());
                }

                break;

            case "/watchlater":
                replyMessage = sendFavoriteMovies(chatId, userId, false);
                logger.info("User {} pressed '/watchlater'", message.getFrom().getUserName());
                break;

            case "/watched":
                replyMessage = sendFavoriteMovies(chatId, userId, true);
                logger.info("User {} pressed '/watched'", message.getFrom().getUserName());
                break;

            case "/subscribe":
                replyMessage = setSubscription(message, true);
                logger.info("User {} has subscribed", message.getFrom().getUserName());
                break;

            case "/unsubscribe":
                replyMessage = setSubscription(message, false);
                logger.info("User {} has unsubscribed", message.getFrom().getUserName());
                break;

            case "/sendlog":
                logger.info("User {} pressed '/senlog'", message.getFrom().getUserName());
                replyMessage = sendLastLog(chatId);
                break;

            default:
                replyMessage = replyUtil.textReply(chatId, "Извините, я не знаю такой команды...");
                logger.info("User {} entered unknown command: '{}'", message.getFrom().getUserName(), message.getText());
                break;

        }
        return replyMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.DEFAULT;
    }

    private SendMessage sendFavoriteMovies(long chatId, long userId, boolean isWatched) {

        SendMessage replyMessage;
        String reply = isWatched ? "Ваш список просмотренных фильмов" : "Ваш список фильмов к просмотру";

        List<Movie> movies = movieService.getMoviesByUserAndIsWatched(userId, isWatched);

        if (!movies.isEmpty()) {
            replyMessage = replyUtil.textReply(chatId, reply);

            movies.forEach(movie1 -> {

                SendMessage movieMessage = replyUtil.textReply(chatId, replyUtil.shortMovieDescription(movie1));

                if (isWatched) {
                    movieMessage.setReplyMarkup(Keyboards.getWatchedMoviesButtons(movie1));
                } else {
                    movieMessage.setReplyMarkup(Keyboards.getUnwatchedMoviesButtons(movie1));
                }

                bot.executeMessage(movieMessage);
            });

        } else {
            replyMessage = replyUtil.textReply(chatId, "Вы пока не добавили ни одного фильма");
        }
        return replyMessage;
    }

    private SendMessage setSubscription(Message message, boolean isSubscribed) {
        SendMessage replyMessage;

        TgUser user = tgUserService.getOrCreateUserFromMessage(message);
        UserChoiceData userChoiceData = userChoiceDataService.getUserChoiceData(user).orElse(null);

        if (isSubscribed) {
            if (userChoiceData != null && userChoiceData.getRating() != null) {
                try {
                    tgUserService.setUsersSubscriptionStatus(user, true);
                    replyMessage = replyUtil.textReply(message.getChatId(), "Вы подписались на рассылку!");
                } catch (Exception e) {
                    replyMessage = replyUtil.textReply(message.getChatId(), "Произошла ошибка. Попробуйте снова.");
                }
            } else {
                replyMessage = replyUtil.textReply(message.getChatId(), "Вы еще не указали предпочтения для поиска. " +
                                                                                "Нажмите /preferences, чтобы это исправить.");
            }
        } else {
            try {
                tgUserService.setUsersSubscriptionStatus(user, false);
                replyMessage = replyUtil.textReply(message.getChatId(), "Вы отписались от рассылки!");
            } catch (Exception e) {
                replyMessage = replyUtil.textReply(message.getChatId(), "Произошла ошибка. Попробуйте снова.");
            }
        }

        return replyMessage;
    }

    private SendMessage sendLastLog(long chatId) {
        SendMessage replyMessage;

        if (chatId == adminChatId) {
            SendDocument document = new SendDocument();
            document.setChatId(chatId);
            document.setDocument(new InputFile().setMedia(replyUtil.getLastLog()));
            bot.executeDocument(document);

            logger.info("Log has been sent");

            replyMessage = replyUtil.textReply(chatId, "Последний log-файл");
        } else {
            replyMessage = replyUtil.textReply(chatId, "У вас недостаточно прав");
            logger.info("Log has not been sent. User is not admin");
        }
        return replyMessage;
    }


}
