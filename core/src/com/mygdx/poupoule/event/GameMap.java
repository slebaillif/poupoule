package com.mygdx.poupoule.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameMap {
    @JsonProperty
    String name;
    @JsonProperty
    String tileFile;
    @JsonProperty
    List<GameLocation> locations;
}
