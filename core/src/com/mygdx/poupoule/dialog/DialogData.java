package com.mygdx.poupoule.dialog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mygdx.poupoule.SimpleCoord;

import java.util.List;
import java.util.Optional;

public class DialogData {
    @JsonProperty
    String npc;
    @JsonProperty
    String title;
    @JsonProperty
    String portrait;
    @JsonProperty
    String startingPoint;
    @JsonProperty
    List<Exchange> exchanges;
    @JsonProperty
    SimpleCoord exit;

    public Exchange getExchange(String name) {
        Optional<Exchange> result = exchanges.stream().filter(e -> e.name.equals(name)).findFirst();
        return result.orElse(null);
    }

    public String getNpc() {
        return npc;
    }

    public String getTitle() {
        return title;
    }

    public String getPortrait() {
        return portrait;
    }
}
