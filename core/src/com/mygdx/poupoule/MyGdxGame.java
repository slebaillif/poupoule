package com.mygdx.poupoule;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    TextureRegion firstRegion;
    TextureRegion[] green = new TextureRegion[9];
    List<TextureRegion> theMap;
    TiledMap currentMap;
    TiledMap villageMap;
    TiledMap guildMap;
    float unitScale = 1 / 16f;
    float renderRatio;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    PlayerCoord playerCoord = new PlayerCoord(13, 15);
    Texture princess;
    Sprite princessSprite;
    MyInputProcessor inputProcessor;

    @Override
    public void create() {
        batch = new SpriteBatch();

        villageMap = new TmxMapLoader().load("TILESET\\village.tmx");
        guildMap = new TmxMapLoader().load("TILESET\\guild.tmx");
        currentMap = villageMap;

        princess = new Texture("SPRITES\\HEROS\\PRINCESS\\HEROS_PixelPackTOPDOWN8BIT_Princess Idle D.gif");

        renderer = new OrthogonalTiledMapRenderer(villageMap, unitScale);

        princessSprite = new Sprite(princess);

    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        renderer.setView(camera);
        renderer.render();

        renderer.getBatch().begin();
        renderer.getBatch().draw(princessSprite, playerCoord.x, playerCoord.y, 1, 1);
        renderer.getBatch().end();

        checkCoordinateEvent();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera = new OrthographicCamera();
        renderRatio = (float) (width) / (float) height;
        camera.setToOrtho(false, 16 * (renderRatio), 16);

        inputProcessor = new MyInputProcessor(camera, currentMap, playerCoord, renderer);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    public void checkCoordinateEvent() {
        if (currentMap == villageMap && playerCoord.x == 15 && playerCoord.y == 13) {
            // enter guild
            renderer = new OrthogonalTiledMapRenderer(guildMap, unitScale);
            playerCoord.x = 12;
            playerCoord.y = 2;
            currentMap = guildMap;
            camera = new OrthographicCamera();
            camera.setToOrtho(false, 16 * (renderRatio), 16);
            renderer.setView(camera);
            inputProcessor = new MyInputProcessor(camera, currentMap, playerCoord, renderer);
            Gdx.input.setInputProcessor(inputProcessor);
        }
    }
}
