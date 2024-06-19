package com.mygdx.poupoule.combat;

public class PlayerAction {
    String name;
    TargetType targetType;
    ActionEffect effect;

    public PlayerAction(String name, TargetType targetType, ActionEffect effect) {
        this.name = name;
        this.targetType = targetType;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public ActionEffect getEffect() {
        return effect;
    }
}
