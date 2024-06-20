package com.mygdx.poupoule.quest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Condition {
    @JsonProperty
    String item;
    @JsonProperty
    Integer quantity;

    public Condition() {
    }

    public String getItem() {
        return item;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
