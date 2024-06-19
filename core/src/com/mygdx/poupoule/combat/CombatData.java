package com.mygdx.poupoule.combat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mygdx.poupoule.SimpleCoord;
import com.mygdx.poupoule.inventory.Stackable;

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
    @JsonProperty
    List<Stackable> loots;


    public CombatData() {
    }
}
