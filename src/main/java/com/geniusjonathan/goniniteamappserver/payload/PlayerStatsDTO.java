package com.geniusjonathan.goniniteamappserver.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerStatsDTO {

    private Integer points;

    private Integer  ft_m;

    private Integer ft_a;

    private Integer threepoint_made;

    private Integer fouls;

}
