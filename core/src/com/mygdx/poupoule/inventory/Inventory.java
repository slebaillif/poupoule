package com.mygdx.poupoule.inventory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    String weapon;
    String armor;
    Map<String, Stackable> questThings = new HashMap<>();

    public Inventory(String weapon, String armor) {
        this.weapon = weapon;
        this.armor = armor;
    }

    public void add(List<Stackable> loot) {
        for (Stackable s : loot) {
            Stackable current = questThings.get(s.name);
            if (current == null) {
                questThings.put(s.name, s);
            } else {
                Stackable added = current.add(s);
                questThings.put(added.name, added);
            }
        }
    }

    public void remove(Stackable cost) {
        Stackable current = questThings.get(cost.name);
        if (current != null) {
            Stackable removed = current.substract(cost);
            questThings.put(removed.name, removed);
        }
    }

    public String getWeapon() {
        return weapon;
    }

    public String getArmor() {
        return armor;
    }

    public Collection<Stackable> getQuestThings() {
        return questThings.values();
    }

    public boolean canAfford(Stackable cost) {
        Stackable t = questThings.get(cost.getName());
        if (t != null) {
            return t.count >= cost.getCount();
        } else {
            return false;
        }
    }
}
