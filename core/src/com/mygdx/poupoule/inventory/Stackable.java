package com.mygdx.poupoule.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stackable {
    @JsonProperty
    String name;
    @JsonProperty
    Integer count = 0;
    @JsonProperty
    ItemType itemType;

    public Stackable() {
    }

    public Stackable(String name, ItemType itemType) {
        this.name = name;
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void addItem() {
        this.count++;
    }

    public void removeItem() {
        this.count--;
    }
}
