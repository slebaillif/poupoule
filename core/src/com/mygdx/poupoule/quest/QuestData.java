package com.mygdx.poupoule.quest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QuestData {
    @JsonProperty
    String missionName;
    @JsonProperty
    List<Condition> fullFillConditions;
    @JsonProperty
    String reward;

    public QuestData() {
    }

    public String getMissionName() {
        return missionName;
    }

    public List<Condition> getFullFillConditions() {
        return fullFillConditions;
    }

    public String getReward() {
        return reward;
    }
}
