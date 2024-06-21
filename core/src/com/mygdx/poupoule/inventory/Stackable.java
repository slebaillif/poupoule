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

    public Stackable(String name, ItemType itemType, int count) {
        this.name = name;
        this.itemType = itemType;
        this.count = count;
    }

    public Stackable add(Stackable second) {
        if (!this.name.equals(second.name)){
            throw new IllegalArgumentException("adding different items.");
        }
        return new Stackable(this.name, this.itemType, this.getCount() + second.getCount());
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
