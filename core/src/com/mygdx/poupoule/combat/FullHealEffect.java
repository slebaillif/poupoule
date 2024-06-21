package com.mygdx.poupoule.combat;

public class FullHealEffect extends ActionEffect {
    @Override
    public String execute(Monster m) {
        m.currentHitPoints = m.hitPoints;
        return ""+m.name +" returns to full health";
    }

    @Override
    public String execute(MainCharacter m) {
        m.currentHitPoints = m.hitPoints;
        return "Hero returns to full health";
    }
}
