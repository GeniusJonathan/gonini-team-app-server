package com.geniusjonathan.goniniteamappserver.controller;

import com.geniusjonathan.goniniteamappserver.exception.ResourceNotFoundException;
import com.geniusjonathan.goniniteamappserver.model.Team;
import com.geniusjonathan.goniniteamappserver.repository.TeamRepository;
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
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable(value = "id") Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));
    }

    @PostMapping
    public Team createTeam(@Valid @RequestBody Team team) {
        return teamRepository.save(team);
    }

    @PutMapping("/{id}")
    public Team updateTeam(@PathVariable(value = "id") Long teamId, @Valid @RequestBody Team updatedTeam) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->new ResourceNotFoundException("Team", "id", teamId));

        team.setName(updatedTeam.getName());
        team.setAddress(updatedTeam.getAddress());
        team.setCity(updatedTeam.getCity());
        team.setPostalCode(updatedTeam.getPostalCode());

        return teamRepository.save(team);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->new ResourceNotFoundException("Team", "id", teamId));

        teamRepository.delete(team);

        return ResponseEntity.ok().build();
    }


}
