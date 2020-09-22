package com.geniusjonathan.goniniteamappserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "player_availability", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "game_id",
                "player_id"
        })
})
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PlayerAvailability {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private Boolean isAvailable;

    public PlayerAvailability() {}

    public PlayerAvailability(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.isAvailable =  false;
    }

}
