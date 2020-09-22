package com.geniusjonathan.goniniteamappserver.payload;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PlayerAvailabilityDTO {

    @NotNull
    private Boolean isAvailable;
}
