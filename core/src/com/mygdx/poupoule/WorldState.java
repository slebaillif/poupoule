package com.mygdx.poupoule;

import java.util.HashMap;
import java.util.Map;

public class WorldState {
    Map<String, Boolean> resolvedCombats = new HashMap<>(); // combat name by map

    public WorldState() {
    }

    public void addResolvedCombat(String map, String combat) {
        resolvedCombats.put(map + combat, true);
    }

    public boolean isCombatResolved(String map, String combat) {
        return resolvedCombats.get(map + combat) != null;
    }
}
