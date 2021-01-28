package com.geniusjonathan.goniniteamappserver.service;

import com.geniusjonathan.goniniteamappserver.exception.ResourceNotFoundException;
import com.geniusjonathan.goniniteamappserver.model.*;
import com.geniusjonathan.goniniteamappserver.payload.GameDTO;
import com.geniusjonathan.goniniteamappserver.repository.GameRepository;
import com.geniusjonathan.goniniteamappserver.repository.PlayerAvailabilityRepository;
import com.geniusjonathan.goniniteamappserver.repository.PlayerRepository;
import com.geniusjonathan.goniniteamappserver.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerAvailabilityRepository playerAvailabilityRepository;

    @Captor
    private ArgumentCaptor<Game> gameArgumentCaptor;

    @Rule
    private ExpectedException exceptionRule =  ExpectedException.none();

    private GameService gameService;
    private Team teamA;
    private Team teamB;
    private Player playerA;
    private Player playerB;
    private List<Player> playerList;
    private Game game;

//    @InjectMocks
//    private GameService gameService;

    @BeforeEach
    private void setup() {
        gameService = new GameService(teamRepository, gameRepository, playerRepository, playerAvailabilityRepository);

        teamA = new Team();
        teamA.setId(1L);
        teamA.setName("Team A");
        Optional<Team> test = Optional.of(teamA);

        teamB = new Team();
        teamB.setId(2L);
        teamB.setName("Team B");

        playerA = new Player();
        playerA.setId(1L);
        playerA.setFirstName("Player");
        playerA.setLastName("A");

        playerB = new Player();
        playerB.setId(1L);
        playerB.setFirstName("Player");
        playerB.setLastName("B");

        playerList = Arrays.asList(playerA, playerB);

        game = new Game();
        game.setId(1L);
        game.setHomeTeam(teamA);
        game.setAwayTeam(teamB);
        game.setDate("22/01/2020");
        game.setTime("15:00");
        game.setDetails("Some Details");
    }

    @Test
    @DisplayName("Should successfully create a new game without score and result")
    public void createNewGameSuccessfullyTest() {

        GameDTO request = new GameDTO();
        request.setHomeTeamId(1L);
        request.setAwayTeamId(2L);
        request.setDate("22/01/2020");
        request.setTime("15:00");
        request.setDetails("Some Details");

        Mockito.when(teamRepository.findById(1L)).thenReturn(Optional.of(teamA));
        Mockito.when(teamRepository.findById(2L)).thenReturn(Optional.of(teamB));
        Mockito.when(playerRepository.findAll()).thenReturn(playerList);

        Game actualGame = gameService.createGame(request);

        Mockito.verify(gameRepository, Mockito.times(1)).save(gameArgumentCaptor.capture());
        Assertions.assertThat(gameArgumentCaptor.getValue().getHomeTeam()).isEqualTo(teamA);
        Assertions.assertThat(gameArgumentCaptor.getValue().getAwayTeam()).isEqualTo(teamB);
        Assertions.assertThat(gameArgumentCaptor.getValue().getDate()).isEqualTo("22/01/2020");
        Assertions.assertThat(gameArgumentCaptor.getValue().getTime()).isEqualTo("15:00");
        Assertions.assertThat(gameArgumentCaptor.getValue().getDetails()).isEqualTo("Some Details");
        Assertions.assertThat(gameArgumentCaptor.getValue().getScore()).isEqualTo("00 - 00");
        Assertions.assertThat(gameArgumentCaptor.getValue().getIsWon()).isEqualTo(false);

        Mockito.verify(playerAvailabilityRepository, Mockito.times(2)).save(Mockito.any(PlayerAvailability.class));
    }

    @Test
    @DisplayName("Should successfully create a new game with score and result")
    public void createNewGameSuccessfullyWithScoresTest() {

        GameDTO request = new GameDTO();
        request.setHomeTeamId(1L);
        request.setAwayTeamId(2L);
        request.setDate("22/01/2020");
        request.setTime("15:00");
        request.setDetails("Some Details");
        request.setScore("100 - 80");
        request.setIsWon(true);

        Mockito.when(teamRepository.findById(1L)).thenReturn(Optional.of(teamA));
        Mockito.when(teamRepository.findById(2L)).thenReturn(Optional.of(teamB));
        Mockito.when(playerRepository.findAll()).thenReturn(playerList);

        Game actualGame = gameService.createGame(request);

        Mockito.verify(gameRepository, Mockito.times(1)).save(gameArgumentCaptor.capture());
        Assertions.assertThat(gameArgumentCaptor.getValue().getHomeTeam()).isEqualTo(teamA);
        Assertions.assertThat(gameArgumentCaptor.getValue().getAwayTeam()).isEqualTo(teamB);
        Assertions.assertThat(gameArgumentCaptor.getValue().getDate()).isEqualTo("22/01/2020");
        Assertions.assertThat(gameArgumentCaptor.getValue().getTime()).isEqualTo("15:00");
        Assertions.assertThat(gameArgumentCaptor.getValue().getDetails()).isEqualTo("Some Details");
        Assertions.assertThat(gameArgumentCaptor.getValue().getScore()).isEqualTo("100 - 80");
        Assertions.assertThat(gameArgumentCaptor.getValue().getIsWon()).isEqualTo(true);

        Mockito.verify(playerAvailabilityRepository, Mockito.times(2)).save(Mockito.any(PlayerAvailability.class));
    }

    @Test
    @DisplayName("Should throw No Resource Exception when user creates game with no valid team uid")
    public void createGameThrowsExceptionWhenNoValidTeamId() {

        GameDTO request = new GameDTO();
        request.setHomeTeamId(1L);
        request.setAwayTeamId(2L);
        request.setDate("22/01/2020");
        request.setTime("15:00");
        request.setDetails("Some Details");
        request.setScore("100 - 80");
        request.setIsWon(true);

        Mockito.when(teamRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            Game actualGame = gameService.createGame(request);
        });
    }

    @Test
    @DisplayName("Should successfully update game")
    public void shouldSuccessfullyUpdateGameTest() {

        GameDTO request = new GameDTO();
        request.setHomeTeamId(1L);
        request.setAwayTeamId(2L);
        request.setDate("22/01/2020");
        request.setTime("15:00");
        request.setDetails("Some Details");
        request.setScore("100 - 80");
        request.setIsWon(true);

        Mockito.when(teamRepository.findById(1L)).thenReturn(Optional.of(teamA));
        Mockito.when(teamRepository.findById(2L)).thenReturn(Optional.of(teamB));
        Mockito.when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        Game actualGame = gameService.updateGame(1L, request);

        Mockito.verify(gameRepository, Mockito.times(1)).save(gameArgumentCaptor.capture());
        Assertions.assertThat(gameArgumentCaptor.getValue().getHomeTeam()).isEqualTo(teamA);
        Assertions.assertThat(gameArgumentCaptor.getValue().getAwayTeam()).isEqualTo(teamB);
        Assertions.assertThat(gameArgumentCaptor.getValue().getDate()).isEqualTo("22/01/2020");
        Assertions.assertThat(gameArgumentCaptor.getValue().getTime()).isEqualTo("15:00");
        Assertions.assertThat(gameArgumentCaptor.getValue().getDetails()).isEqualTo("Some Details");
        Assertions.assertThat(gameArgumentCaptor.getValue().getScore()).isEqualTo("100 - 80");
        Assertions.assertThat(gameArgumentCaptor.getValue().getIsWon()).isEqualTo(true);

    }

    @Test
    @DisplayName("Should throw exception when no valid game is given when updating game")
    public void shouldThrowExceptionWhenNoValidGameUpdateGameTest() {
        GameDTO request = new GameDTO();
        request.setHomeTeamId(1L);
        request.setAwayTeamId(2L);

        Mockito.when(teamRepository.findById(Matchers.anyLong())).thenReturn(Optional.of(teamA));
        Mockito.when(gameRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            Game actualGame = gameService.updateGame(1L, request);
        });
    }

    @Test
    @DisplayName("Should successfully delete game and delete all player availability for specified game")
    public void shouldDeleteGameSuccessfully() {
        PlayerAvailability playerAvailabilityA = new PlayerAvailability();
        playerAvailabilityA.setId(1L);
        playerAvailabilityA.setGame(game);
        playerAvailabilityA.setPlayer(playerA);
        playerAvailabilityA.setStatus(AvailabilityStatus.UNKNOWN);

        PlayerAvailability playerAvailabilityB = new PlayerAvailability();
        playerAvailabilityB.setId(2L);
        playerAvailabilityB.setGame(game);
        playerAvailabilityB.setPlayer(playerB);
        playerAvailabilityB.setStatus(AvailabilityStatus.UNKNOWN);

        List<PlayerAvailability> playerAvailabilityList = Arrays.asList(playerAvailabilityA, playerAvailabilityB);


        Mockito.when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        Mockito.when(playerAvailabilityRepository.findByGame(game)).thenReturn(playerAvailabilityList);

        gameService.deleteGame(1L);

        Mockito.verify(playerAvailabilityRepository, Mockito.times(2)).delete(Matchers.any(PlayerAvailability.class));
        Mockito.verify(gameRepository, Mockito.times(1)).delete(game);

    }
}