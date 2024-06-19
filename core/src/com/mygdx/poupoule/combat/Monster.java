package com.mygdx.poupoule.combat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Monster {
    String name;
    String type;
    Integer hitPoints;
    Integer defense;
    Integer attack;
    String attackName;

    Integer currentHitPoints;
    String imagePath;
    Image monsterImage;

    public Monster(String name, String type, Integer hitPoints, Integer defense, Integer attack, String attackName, String imagePath) {
        this.name = name;
        this.type = type;
        this.hitPoints = hitPoints;
        this.defense = defense;
        this.attack = attack;
        this.attackName = attackName;
        this.currentHitPoints = hitPoints;
        this.imagePath = imagePath;
        this.monsterImage = new Image(new Texture(imagePath));

    }

    public void isHit(Integer damage) {
        currentHitPoints = currentHitPoints - damage;
    }

    public boolean isAlive() {
        return currentHitPoints > 0;
    }

    public Image getMonsterImage() {
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
