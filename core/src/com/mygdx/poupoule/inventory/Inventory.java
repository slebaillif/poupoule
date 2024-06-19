package com.mygdx.poupoule.inventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    String weapon;
    String armor;
    List<Stackable> questThings = new ArrayList<>();

    public Inventory(String weapon, String armor) {
        this.weapon = weapon;
        this.armor = armor;
    }

    public void add(List<Stackable> loot) {
        questThings.addAll(loot);
    }

    public String getWeapon() {
        return weapon;
    }

    public String getArmor() {
        return armor;
    }

    public List<Stackable> getQuestThings() {
        return questThings;
    }
}
