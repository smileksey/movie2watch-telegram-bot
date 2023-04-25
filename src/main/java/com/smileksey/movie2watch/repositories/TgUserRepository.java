package com.smileksey.movie2watch.repositories;

import com.smileksey.movie2watch.models.TgUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TgUserRepository extends JpaRepository<TgUser, Long> {
}
