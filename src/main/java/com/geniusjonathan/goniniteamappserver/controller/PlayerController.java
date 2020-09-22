package com.geniusjonathan.goniniteamappserver.controller;

import com.geniusjonathan.goniniteamappserver.exception.ResourceNotFoundException;
import com.geniusjonathan.goniniteamappserver.model.Player;
import com.geniusjonathan.goniniteamappserver.repository.PlayerRepository;
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
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/test")
    public String getHelloDocker() {
        return "Hello Docker";
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable(value = "id") Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", playerId));
    }

    @PostMapping
    public Player createPlayer(@Valid @RequestBody Player player) {
        return playerRepository.save(player);
    }

    @PutMapping("/{playerId}")
    public Player updatePlayer(@PathVariable(value = "playerId") Long playerId, @Valid @RequestBody Player updatedPlayer) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() ->new ResourceNotFoundException("Player", "id", playerId));

        player.setFirstName(updatedPlayer.getFirstName());
        player.setLastName(updatedPlayer.getLastName());

        return playerRepository.save(player);
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() ->new ResourceNotFoundException("Player", "id", playerId));

        playerRepository.delete(player);

        return ResponseEntity.ok().build();
    }


}
