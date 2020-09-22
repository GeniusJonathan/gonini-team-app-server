package com.geniusjonathan.goniniteamappserver.repository;

import com.geniusjonathan.goniniteamappserver.model.Game;
import com.geniusjonathan.goniniteamappserver.model.Player;
import com.geniusjonathan.goniniteamappserver.model.PlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long> {

    List<PlayerStats> findByGame(Game game);
    List<PlayerStats> findByGameAndPlayer(Game game, Player player);
}
