package com.mygdx.poupoule.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.poupoule.CurrentSceneType;
import com.mygdx.poupoule.MyGdxGame;
import com.mygdx.poupoule.TiledMapInputProcessor;

import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.badlogic.gdx.Input.Keys.SPACE;

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
            theGame.playerCoord = currentDialog.getExitCoord();
            theGame.inputProcessor = new TiledMapInputProcessor(theGame.camera, theGame.currentMap, theGame.playerCoord, theGame.renderer);
            Gdx.input.setInputProcessor(theGame.inputProcessor);
            theGame.currentStageType = CurrentSceneType.TiledMap;
            return true;
        } else {
            return false;
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
