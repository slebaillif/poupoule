package com.mygdx.poupoule.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.poupoule.CurrentSceneType;
import com.mygdx.poupoule.MyGdxGame;
import com.mygdx.poupoule.TiledMapInputProcessor;

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
        theGame.playerCoord = currentDialog.getExitCoord();
        theGame.inputProcessor = new TiledMapInputProcessor(theGame.camera, theGame.currentMap, theGame.playerCoord, theGame.renderer);
        Gdx.input.setInputProcessor(theGame.inputProcessor);
        theGame.currentStageType = CurrentSceneType.TiledMap;
    }

    private void selectPlayerResponse(int lineSelected) {
        if (lineSelected > 0 && currentDialog.getPlayerOptions().size() >= lineSelected) {
            if (currentDialog.getPlayerOptions().get(lineSelected - 1).nextExchange.equalsIgnoreCase("end")){
                exitToMap();
            }else {
                String newDialog = currentDialog.getPlayerOptions().get(lineSelected - 1).nextExchange;
                currentDialog.setCurrentDialog(newDialog);
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
