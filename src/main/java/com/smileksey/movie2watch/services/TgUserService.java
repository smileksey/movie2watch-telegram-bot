package com.smileksey.movie2watch.services;

import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.repositories.TgUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TgUserService {
    private final TgUserRepository tgUserRepository;

    @Autowired
    public TgUserService(TgUserRepository tgUserRepository) {
        this.tgUserRepository = tgUserRepository;
    }

    @Transactional
    public void save(TgUser user) {
        tgUserRepository.save(user);
    }

    public Optional<TgUser> getUserById(long userId) {
        return tgUserRepository.findById(userId);
    }

    @Transactional
    public TgUser getOrCreateUserFromMessage(Message message) {

        Optional<TgUser> optionalTgUser = getUserById(message.getFrom().getId());
        TgUser tgUser;

        if (optionalTgUser.isPresent()) {
            tgUser = optionalTgUser.get();
        } else {
            tgUser = new TgUser();

            tgUser.setId(message.getFrom().getId());
            tgUser.setChatId(message.getChatId());
            tgUser.setUserName(message.getFrom().getUserName());
            tgUser.setFirstName(message.getFrom().getFirstName());

            save(tgUser);
        }
        return tgUser;
    }

    public List<TgUser> getSubscribedUsers() {
        return tgUserRepository.findAllByIsSubscribed(true);
    }

    @Transactional
    public void setUsersSubscriptionStatus(TgUser user, boolean isSubscribed) {
        user.setSubscribed(isSubscribed);
        save(user);
    }
}
