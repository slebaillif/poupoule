package com.mygdx.poupoule.dialog;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Exchange {
    @JsonProperty
    String name;
    @JsonProperty
    List<String> npcSpeech;
    @JsonProperty
    List<PlayerResponseResult> playerResponses;
}
