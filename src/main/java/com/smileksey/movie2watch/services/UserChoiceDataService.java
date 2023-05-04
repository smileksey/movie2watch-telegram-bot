package com.smileksey.movie2watch.services;

import com.smileksey.movie2watch.models.UserChoiceData;
import com.smileksey.movie2watch.models.TgUser;
import com.smileksey.movie2watch.repositories.UserChoiceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserChoiceDataService {
    private final UserChoiceDataRepository userChoiceDataRepository;

    @Autowired
    public UserChoiceDataService(UserChoiceDataRepository userChoiceDataRepository) {
        this.userChoiceDataRepository = userChoiceDataRepository;
    }

    @Transactional
    public void save(UserChoiceData userChoiceData) {
        userChoiceDataRepository.save(userChoiceData);
    }

    public Optional<UserChoiceData> getUserChoiceData(TgUser tgUser) {
        return userChoiceDataRepository.findUserChoiceDataByTgUser(tgUser);
    }

    @Transactional
    public UserChoiceData getOrCreateUserChoiceData(TgUser tgUser) {

        UserChoiceData choiceData;
        Optional<UserChoiceData> optionalUserChoiceData = getUserChoiceData(tgUser);

        if (optionalUserChoiceData.isPresent()) {
            choiceData = optionalUserChoiceData.get();
        } else {
            choiceData = new UserChoiceData();
            choiceData.setTgUser(tgUser);
            save(choiceData);
        }
        return choiceData;
    }

}
