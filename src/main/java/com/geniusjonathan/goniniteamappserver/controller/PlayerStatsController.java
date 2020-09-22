package com.geniusjonathan.goniniteamappserver.controller;

import com.geniusjonathan.goniniteamappserver.exception.ResourceNotFoundException;
import com.geniusjonathan.goniniteamappserver.model.Game;
import com.geniusjonathan.goniniteamappserver.model.Player;
import com.geniusjonathan.goniniteamappserver.model.PlayerStats;
import com.geniusjonathan.goniniteamappserver.payload.PlayerStatsDTO;
import com.geniusjonathan.goniniteamappserver.repository.GameRepository;
import com.geniusjonathan.goniniteamappserver.repository.PlayerRepository;
import com.geniusjonathan.goniniteamappserver.repository.PlayerStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PlayerStatsController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    @GetMapping("/games/{gameId}/stats")
    public List<PlayerStats> getStatsForGame(@PathVariable Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game", "game_id", gameId));
        return playerStatsRepository.findByGame(game);
    }

    @GetMapping("/games/{gameId}/stats/{playerId}")
    public PlayerStats getStatsForGame(@PathVariable Long gameId, @PathVariable Long playerId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game", "game_id", gameId));
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new ResourceNotFoundException("Player", "player_id", playerId));
        return playerStatsRepository.findByGameAndPlayer(game, player).get(0);
    }

    @PostMapping("/games/{gameId}/stats/{playerId}")
    public PlayerStats addPlayerStatForGame(@PathVariable Long gameId,
                                            @PathVariable Long playerId,
                                            @Valid @RequestBody PlayerStatsDTO playerStats) {

        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game", "game_id", gameId));
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new ResourceNotFoundException("Player", "player_id", playerId));
        PlayerStats stats = new PlayerStats();
        stats.setGame(game);
        stats.setPlayer(player);
        stats.setPoints(playerStats.getPoints());
        stats.setFt_a(playerStats.getFt_a());
        stats.setFt_m(playerStats.getFt_m());
        stats.setThreepoint_made(playerStats.getThreepoint_made());
        stats.setFouls(playerStats.getFouls());

         return playerStatsRepository.save(stats);
    }
}
