package com.mygdx.poupoule.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mygdx.poupoule.PlayerCoord;

public class GameLocation {
    @JsonProperty
    PlayerCoord coordinates;
    @JsonProperty
    EventDetails eventDetails;
}
