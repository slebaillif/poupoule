package com.mygdx.poupoule.combat;

public class MainCharacter {
    String name;
    Integer attack;
    Integer defense;
    Integer hitPoints;
    Integer currentHitPoints;

    public MainCharacter(String name, Integer attack, Integer defense, Integer hitPoints) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.hitPoints = hitPoints;
        this.currentHitPoints = hitPoints;
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
}
