package com.smileksey.movie2watch.repositories;

import com.smileksey.movie2watch.models.UserChoiceData;
import com.smileksey.movie2watch.models.TgUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChoiceDataRepository extends JpaRepository<UserChoiceData, Integer> {
    Optional<UserChoiceData> findUserChoiceDataByTgUser(TgUser tgUser);
}
