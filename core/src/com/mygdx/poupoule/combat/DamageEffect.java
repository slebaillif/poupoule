package com.mygdx.poupoule.combat;

public class DamageEffect extends ActionEffect {
    Integer damage;

    public DamageEffect(Integer damage) {
        this.damage = damage;
    }

    @Override
    public String execute(Monster m) {
        m.isHit(this.damage);
        return "" + m.getName() + " was hit for " + damage + " damage.";
    }
}
