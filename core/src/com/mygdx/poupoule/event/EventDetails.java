package com.mygdx.poupoule.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mygdx.poupoule.PlayerCoord;

public class EventDetails {
    @JsonProperty
    EventType eventType;
    @JsonProperty
    String newMap;
    @JsonProperty
    PlayerCoord coordinates;

    public EventType getEventType() {
        return eventType;
    }

    public String getNewMap() {
        return newMap;
    }

    public PlayerCoord getCoordinates() {
        return coordinates;
    }
}
