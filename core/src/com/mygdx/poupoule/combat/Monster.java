package com.mygdx.poupoule.combat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Monster {
    @JsonProperty
    String name;
    @JsonProperty
    String type;
    @JsonProperty
    Integer hitPoints;
    @JsonProperty
    Integer defense;
    @JsonProperty
    Integer attack;
    @JsonProperty
    String attackName;
    @JsonProperty
    String imagePath;
    @JsonProperty
    Integer currentHitPoints;

    Image monsterImage;

    public Monster() {
    }

    public void isHit(Integer damage) {
        currentHitPoints = currentHitPoints - damage;
    }

    public boolean isAlive() {
        return currentHitPoints > 0;
    }

    public Image getMonsterImage() {
        if (monsterImage == null) {
            this.monsterImage = new Image(new Texture(imagePath));
        }
        return monsterImage;
    }

    public String getName() {
        return name;
    }

    public Integer getCurrentHitPoints() {
        return currentHitPoints;
    }

    public String getType() {
        return type;
    }

    public Integer getHitPoints() {
        return hitPoints;
    }

    public Integer getDefense() {
        return defense;
    }

    public Integer getAttack() {
        return attack;
    }

    public String getAttackName() {
        return attackName;
    }

}
