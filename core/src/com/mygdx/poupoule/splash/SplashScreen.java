package com.mygdx.poupoule.splash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.poupoule.CurrentSceneType;
import com.mygdx.poupoule.MyGdxGame;

import static com.badlogic.gdx.Input.Keys.SPACE;

public class SplashScreen implements InputProcessor {
    Skin skin;
    FitViewport viewport;
    Stage dialogStage;
    MyGdxGame game;

    public SplashScreen(Skin skin, MyGdxGame game, float width, float height) {
        this.skin = skin;
        this.game = game;
        viewport = new FitViewport(width, height);
    }

    public Stage createStage() {
        dialogStage = new Stage(viewport);
        Gdx.input.setInputProcessor(this);

        Table table = new Table();
        table.top().center();
        table.add( new Label("PROJET POUPOULE", skin)).expandX().center();
        table.row();
        table.add( new Label("Press Space bar", skin)).expandX().center();
        table.row();
        Stack stack = new Stack(table);
        stack.setFillParent(true);

        dialogStage.addActor(stack);
        return dialogStage;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == SPACE) {
            game.changeSceneType(CurrentSceneType.TiledMap);
            return true;
        }
        return false;
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
