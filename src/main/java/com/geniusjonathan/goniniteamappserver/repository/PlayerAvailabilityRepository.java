package com.geniusjonathan.goniniteamappserver.repository;

import com.geniusjonathan.goniniteamappserver.model.Game;
import com.geniusjonathan.goniniteamappserver.model.Player;
import com.geniusjonathan.goniniteamappserver.model.PlayerAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerAvailabilityRepository extends JpaRepository<PlayerAvailability, Long> {

    List<PlayerAvailability> findByGame(Game game);
    List<PlayerAvailability> findByGameAndPlayer(Game game, Player player);
}