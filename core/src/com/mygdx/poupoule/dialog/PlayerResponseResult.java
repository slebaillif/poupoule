package com.mygdx.poupoule.dialog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mygdx.poupoule.combat.ActionEffect;
import com.mygdx.poupoule.combat.NamedEffect;
import com.mygdx.poupoule.inventory.Stackable;

public class PlayerResponseResult {
    @JsonProperty
    String line;
    @JsonProperty
    String nextExchange;
    @JsonProperty
    NamedEffect effect;
    @JsonProperty
    Stackable cost;

    public PlayerResponseResult() {
    }

    public PlayerResponseResult(String playerResponse, String result) {
        this.line = playerResponse;
        this.nextExchange = result;
    }

    public String getLine() {
        return line;
    }

    public String getNextExchange() {
        return nextExchange;
    }

    public ActionEffect getEffect() {
        return effect;
    }

    public Stackable getCost() {
        return cost;
    }
}
