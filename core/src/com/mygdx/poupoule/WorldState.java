package com.mygdx.poupoule;

import com.mygdx.poupoule.quest.QuestData;

import java.util.HashMap;
import java.util.Map;

public class WorldState {
    Map<String, Boolean> resolvedCombats = new HashMap<>(); // combat name by map
    Map<String, QuestData> activeQuests = new HashMap<>();
    Map<String, QuestData> completedQuests = new HashMap<>();

    public WorldState() {
    }

    public void addResolvedCombat(String map, String combat) {
        resolvedCombats.put(map + combat, true);
    }

    public boolean isCombatResolved(String map, String combat) {
        return resolvedCombats.get(map + combat) != null;
    }

    public void addQuest(String missionName, QuestData questData) {
        activeQuests.put(missionName, questData);
    }

    public boolean isQuestActive(String missionName) {
        return activeQuests.get(missionName) != null;
    }

    public void completeQuest(String name){
        QuestData active = activeQuests.get(name);
        if(active != null){
            activeQuests.remove(name);
            completedQuests.put(name, active);
        }
    }
}
