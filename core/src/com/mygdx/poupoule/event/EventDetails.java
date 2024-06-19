package com.mygdx.poupoule.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mygdx.poupoule.SimpleCoord;

public class EventDetails {
    @JsonProperty
    EventType eventType;
    @JsonProperty
    String newMap;
    @JsonProperty
    SimpleCoord coordinates;

    public EventType getEventType() {
        return eventType;
    }

    public String getNewMap() {
        return newMap;
    }

    public SimpleCoord getCoordinates() {
        return coordinates;
    }
}
