package com.geniusjonathan.goniniteamappserver.service;

import com.geniusjonathan.goniniteamappserver.exception.ResourceNotFoundException;
import com.geniusjonathan.goniniteamappserver.model.Game;
import com.geniusjonathan.goniniteamappserver.model.Player;
import com.geniusjonathan.goniniteamappserver.model.PlayerAvailability;
import com.geniusjonathan.goniniteamappserver.model.Team;
import com.geniusjonathan.goniniteamappserver.payload.GameDTO;
import com.geniusjonathan.goniniteamappserver.payload.PlayerAvailabilityDTO;
import com.geniusjonathan.goniniteamappserver.repository.GameRepository;
import com.geniusjonathan.goniniteamappserver.repository.PlayerAvailabilityRepository;
import com.geniusjonathan.goniniteamappserver.repository.PlayerRepository;
import com.geniusjonathan.goniniteamappserver.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerAvailabilityRepository playerAvailabilityRepository;

    public Game createGame(GameDTO request) {
        Team home = teamRepository.findById(request.getHomeTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Teams", "id", request.getHomeTeamId()));

        Team away = teamRepository.findById(request.getAwayTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Teams", "id", request.getAwayTeamId()));

        Game game = new Game();
        game.setHomeTeam(home);
        game.setAwayTeam(away);
        game.setDate(request.getDate());
        game.setTime(request.getTime());
        game.setDetails(request.getDetails());
        if (request.getScore() == null) {
            game.setScore("00 - 00");
        } else {
            game.setScore(request.getScore());
        }
        if (request.getIsWon() == null) {
            game.setIsWon(false);
        } else {
            game.setIsWon(request.getIsWon());
        }

        Game savedGame = gameRepository.save(game);
        createPlayerAvailabilityForGame(savedGame);

        return savedGame;
    }

    public Game updateGame(Long gameId, GameDTO updatedGame) {
        Team home = teamRepository.findById(updatedGame.getHomeTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Teams", "id", updatedGame.getHomeTeamId()));

        Team away = teamRepository.findById(updatedGame.getAwayTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Teams", "id", updatedGame.getAwayTeamId()));

        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("GameId", "id", gameId));
        game.setHomeTeam(home);
        game.setAwayTeam(away);
        game.setDate(updatedGame.getDate());
        game.setTime(updatedGame.getTime());
        game.setDetails(updatedGame.getDetails());
        if (updatedGame.getScore() != null) {
            game.setScore(updatedGame.getScore());
        }
        if (updatedGame.getIsWon() != null) {
            game.setIsWon(updatedGame.getIsWon());
        }
        return gameRepository.save(game);
    }

    public void deleteGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));
        playerAvailabilityRepository.findByGame(game).stream().forEach(playerAvailability -> {
            playerAvailabilityRepository.delete(playerAvailability);
        });
        gameRepository.delete(game);
    }

    private void createPlayerAvailabilityForGame(Game game){
        List<Player> playerList = playerRepository.findAll();
        playerList.stream().forEach(player -> {
            PlayerAvailability playerAvailability = new PlayerAvailability(game, player);
            playerAvailabilityRepository.save(playerAvailability);
        });
    }

    public List<PlayerAvailability> getPlayersAvailabilityForGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));
        return playerAvailabilityRepository.findByGame(game);
    }

    public void updateAvailabilityForPlayer(Long gameId, Long playerId, PlayerAvailabilityDTO playerAvailabilityDTO) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new ResourceNotFoundException("Player", "id", playerId));
        PlayerAvailability playerAvailability = playerAvailabilityRepository.findByGameAndPlayer(game, player).get(0);
        playerAvailability.setIsAvailable(playerAvailabilityDTO.getIsAvailable());
        playerAvailabilityRepository.save(playerAvailability);

    }
}
