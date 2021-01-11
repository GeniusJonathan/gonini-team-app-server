package com.geniusjonathan.goniniteamappserver.payload;

import com.geniusjonathan.goniniteamappserver.model.AvailabilityStatus;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PlayerAvailabilityDTO {

    @NotNull
    private String status;
}
