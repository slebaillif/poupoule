package com.mygdx.poupoule.combat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NamedEffect extends ActionEffect {

    @JsonProperty
    String name;

    ActionEffect effect;

    public void create() {
        if (name.equals("fullHeal")) {
            this.effect = new FullHealEffect();
        }
    }

    @Override
    public String execute(Monster m) {
        if (this.effect == null) {
            create();
        }
        return effect.execute(m);
    }

    @Override
    public String execute(MainCharacter m) {
        if (this.effect == null) {
            create();
        }
        return effect.execute(m);
    }
}
