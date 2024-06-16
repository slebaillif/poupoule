package com.mygdx.poupoule.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameEvents {
    @JsonProperty
    List<GameMap> maps;

    public String getMapFileName(String map) {
        Optional<GameMap> opt = maps.stream().filter(m -> m.name.equals(map)).findFirst();
        return opt.map(gameMap -> gameMap.tileFile).orElse(null);
    }

    public List<GameLocation> getLocations(String map) {
        Optional<GameMap> opt = maps.stream().filter(m -> m.name.equals(map)).findFirst();
        return opt.map(gameMap -> gameMap.locations).orElse(Collections.emptyList());
    }

    public EventDetails matchLocation(String map, int x, int y) {
        Optional<GameLocation> opt = getLocations(map).stream().filter(l -> l.coordinates.getX() == x && l.coordinates.getY() == y).findFirst();
        return opt.map(d -> d.eventDetails).orElse(null);
    }

    public List<GameLocation> getEventFromType(String map, EventType type) {
        return getLocations(map).stream().filter(l -> l.eventDetails.getEventType() == type).collect(Collectors.toList());
    }
}
