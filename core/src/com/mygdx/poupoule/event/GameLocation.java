package com.mygdx.poupoule.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mygdx.poupoule.SimpleCoord;

public class GameLocation {
    @JsonProperty
    SimpleCoord coordinates;
    @JsonProperty
    EventDetails eventDetails;

    public SimpleCoord getCoordinates() {
        return coordinates;
    }

    public EventDetails getEventDetails() {
        return eventDetails;
    }
}
