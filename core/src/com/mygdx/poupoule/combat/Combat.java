package com.mygdx.poupoule.combat;

import com.badlogic.gdx.InputProcessor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mygdx.poupoule.CurrentSceneType;
import com.mygdx.poupoule.MyGdxGame;
import com.mygdx.poupoule.SimpleCoord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Input.Keys.*;
import static com.mygdx.poupoule.combat.TargetType.singleMonster;

public class Combat implements InputProcessor {
    CombatData combatData;
    List<PlayerAction> possibleActions = new ArrayList<>();
    MyGdxGame theGame;
    boolean actionMode = true; // false means select target
    PlayerAction slectedAction = null;
    MainCharacter hero;

    public void setCombatData(CombatData combatData) {
        this.combatData = combatData;
    }

    public void setHero(MainCharacter hero) {
        this.hero = hero;
    }

    public void setTheGame(MyGdxGame theGame) {
        this.theGame = theGame;
    }


    public void addActions(List<PlayerAction> m) {
        possibleActions.addAll(m);
    }

    public List<Monster> getMonsters() {
        return combatData.monsters;
    }

    public List<PlayerAction> getPossibleActions() {
        if (allMonstersDefeated()) {
            return possibleActions.stream().filter(a -> a instanceof ExitAction).collect(Collectors.toList());
        } else if (actionMode) {
            return possibleActions;
        } else {
            List<PlayerAction> result = new ArrayList<>();
            List<Monster> aliveMonsters = combatData.monsters.stream().filter(m -> m.isAlive() == true).collect(Collectors.toList());
            for (Monster m : aliveMonsters) {
                result.add(new PlayerAction("Target " + m.getName(), singleMonster, new DamageEffect(1)));
            }
            return result;
        }
    }

    public boolean allMonstersDefeated() {
        List<Monster> aliveMonsters = combatData.monsters.stream().filter(m -> m.isAlive() == true).collect(Collectors.toList());
        return aliveMonsters.isEmpty();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == NUM_1) {
            return performActionOrSelect(0);
        } else if (keycode == NUM_2) {
            return performActionOrSelect(1);
        } else if (keycode == NUM_3) {
            return performActionOrSelect(2);
        }
        return false;
    }

    private boolean performActionOrSelect(int selected) {
        if (actionMode) {
            if (selected >= getPossibleActions().size()) {
                return false;
            }
            PlayerAction action = getPossibleActions().get(selected);
            this.slectedAction = action;
            if (this.slectedAction instanceof ExitAction) {
                theGame.playerCoord.setX(combatData.exitCoordinates.getX());
                theGame.playerCoord.setY(combatData.exitCoordinates.getY());
                theGame.changeSceneType(CurrentSceneType.TiledMap);
                theGame.getWorldState().addResolvedCombat(combatData.map, combatData.combatName);
            }
            if (action.targetType == singleMonster) {
                actionMode = false;
            }

        } else {
            List<Monster> aliveMonsters = combatData.monsters.stream().filter(m -> m.isAlive() == true).collect(Collectors.toList());
            if (selected >= aliveMonsters.size()) {
                return false;
            }
            // select target, resolve action
            Monster target = aliveMonsters.get(selected);
            combatData.display = this.slectedAction.getEffect().execute(target);

            for (Monster m : aliveMonsters) {
                hero.isHit(m.attack);
                combatData.display = m.getName() + " " + m.getAttackName() + " for " + m.attack + " damage.";
            }
            actionMode = true;
        }
        return true;
    }

    public String getDisplay() {
        return combatData.display;
    }


    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
