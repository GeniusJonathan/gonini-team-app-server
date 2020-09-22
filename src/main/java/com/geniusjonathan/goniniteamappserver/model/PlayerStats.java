package com.geniusjonathan.goniniteamappserver.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "player_stats")
@Data
public class PlayerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private Integer points;

    private Integer  ft_m;

    private Integer ft_a;

    private Integer threepoint_made;

    private Integer fouls;

    public PlayerStats() {};


}
