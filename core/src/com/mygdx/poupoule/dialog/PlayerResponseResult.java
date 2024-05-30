package com.mygdx.poupoule.dialog;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerResponseResult {
    @JsonProperty
    String line;
    @JsonProperty
    String nextExchange;

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
}
