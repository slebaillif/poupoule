package com.mygdx.poupoule.combat;

import com.mygdx.poupoule.inventory.Inventory;

public class MainCharacter {
    String name;
    Integer attack;
    Integer defense;
    Integer hitPoints;
    Integer currentHitPoints;
    Inventory inventory;

    public MainCharacter(String name, Integer attack, Integer defense, Integer hitPoints) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.hitPoints = hitPoints;
        this.currentHitPoints = hitPoints;
        this.inventory = new Inventory("Stick", "None");
    }

    public void isHit(Integer damage) {
        this.currentHitPoints = currentHitPoints - damage;
    }

    public String getName() {
        return name;
    }

    public Integer getAttack() {
        return attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public Integer getHitPoints() {
        return hitPoints;
    }

    public Integer getCurrentHitPoints() {
        return currentHitPoints;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
