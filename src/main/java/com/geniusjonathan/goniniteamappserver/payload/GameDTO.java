package com.geniusjonathan.goniniteamappserver.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GameDTO {

    @NotNull
    private Long homeTeamId;

    @NotNull
    private Long awayTeamId;

    @NotNull
    private String Date;

    @NotNull
    private String time;

    private String details;

    private String score;

    private Boolean isWon;

}
