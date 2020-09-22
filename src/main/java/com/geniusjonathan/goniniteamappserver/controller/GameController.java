package com.geniusjonathan.goniniteamappserver.controller;

import com.geniusjonathan.goniniteamappserver.exception.ResourceNotFoundException;
import com.geniusjonathan.goniniteamappserver.model.Game;
import com.geniusjonathan.goniniteamappserver.model.Player;
import com.geniusjonathan.goniniteamappserver.model.PlayerAvailability;
import com.geniusjonathan.goniniteamappserver.payload.GameDTO;
import com.geniusjonathan.goniniteamappserver.payload.PlayerAvailabilityDTO;
import com.geniusjonathan.goniniteamappserver.repository.GameRepository;
import com.geniusjonathan.goniniteamappserver.repository.PlayerRepository;
import com.geniusjonathan.goniniteamappserver.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @GetMapping
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @GetMapping(path = "/{gameId}")
    public Game getGameById(@PathVariable(value = "gameId") Long id ){
        return gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Game", "id", id));
    }

    @PostMapping
    public Game createGame(@Valid @RequestBody GameDTO request) {
        return gameService.createGame(request);
    }

    @PutMapping(path = "/{gameId}")
    public Game updateGame(@PathVariable(value = "gameId") Long id, @Valid @RequestBody GameDTO updatedGame) {
        return gameService.updateGame(id, updatedGame);
    }

    @DeleteMapping(path = "/{gameId}")
    public ResponseEntity<?> deleteGame (@PathVariable(value = "gameId") Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{gameId}/players")
    public List<PlayerAvailability> getPlayerAvailabilityForGame(@PathVariable Long gameId) {
        return gameService.getPlayersAvailabilityForGame(gameId);
    }

    @PutMapping(path = "/{gameId}/players/{playerId}")
    public ResponseEntity<?> updateAvailabilityForPlayer(@PathVariable Long gameId,
                                                         @PathVariable Long playerId,
                                                         @Valid @RequestBody PlayerAvailabilityDTO playerAvailabilityDTO) {

        gameService.updateAvailabilityForPlayer(gameId, playerId, playerAvailabilityDTO);
        return ResponseEntity.ok().build();
    }

}
