package com.mygdx.poupoule.combat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mygdx.poupoule.SimpleCoord;

import java.util.ArrayList;
import java.util.List;

public class CombatData {
    @JsonProperty
    List<Monster> monsters = new ArrayList<>();
    @JsonProperty
    SimpleCoord exitCoordinates;
    @JsonProperty
    String map;
    @JsonProperty
    String combatName;
    @JsonProperty
    String display = "";


    public CombatData() {
    }
}
