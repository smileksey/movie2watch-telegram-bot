package com.smileksey.movie2watch.services;

import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.repositories.TgUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Optional<TgUser> getUser(long userId) {
        return tgUserRepository.findById(userId);
    }
}
