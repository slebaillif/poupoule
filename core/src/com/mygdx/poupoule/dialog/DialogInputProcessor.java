package com.mygdx.poupoule.dialog;

import com.badlogic.gdx.InputProcessor;
import com.mygdx.poupoule.CurrentSceneType;
import com.mygdx.poupoule.MyGdxGame;

import static com.badlogic.gdx.Input.Keys.*;

public class DialogInputProcessor implements InputProcessor {
    GameDialog currentDialog;
    MyGdxGame theGame;

    public DialogInputProcessor(GameDialog newDialog, MyGdxGame theGame) {
        this.currentDialog = newDialog;
        this.theGame = theGame;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == SPACE) {
            currentDialog.moveToNextLine();
            return true;
        } else if (keycode == ESCAPE) {
            exitToMap();
            return true;
        } else if (keycode == NUM_1) {
            selectPlayerResponse(1);
            return true;
        } else if (keycode == NUM_2) {
            selectPlayerResponse(2);
            return true;
        } else if (keycode == NUM_3) {
            selectPlayerResponse(3);
            return true;
        } else if (keycode == NUM_4) {
            selectPlayerResponse(4);
            return true;
        } else if (keycode == NUM_5) {
            selectPlayerResponse(5);
            return true;
        } else {
            return false;
        }
    }

    private void exitToMap() {
        theGame.playerCoord.setX(currentDialog.getExitCoord().getX());
        theGame.playerCoord.setY(currentDialog.getExitCoord().getY());
        theGame.changeSceneType(CurrentSceneType.TiledMap);
    }

    private void selectPlayerResponse(int lineSelected) {
        if (lineSelected > 0 && currentDialog.getPlayerOptions().size() >= lineSelected) {
            PlayerResponseResult playerResponseResult = currentDialog.getPlayerOptions().get(lineSelected - 1);
            if (playerResponseResult.nextExchange.equalsIgnoreCase("end")) {
                exitToMap();
            } else {
                if (playerResponseResult.effect != null) {
                    if (playerResponseResult.cost != null) {
                        if (theGame.hero.getInventory().canAfford(playerResponseResult.cost)) {
                            theGame.hero.getInventory().remove(playerResponseResult.cost);
                            playerResponseResult.effect.execute(theGame.hero);
                        } else {
                            return;
                        }
                    } else {
                        playerResponseResult.effect.execute(theGame.hero);
                    }
                }
                currentDialog.setCurrentDialog(playerResponseResult.nextExchange);
            }
        } else {
            // do nothing
        }

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
