package com.mygdx.poupoule;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;

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
    Texture bobPortrait;
    Sprite princessSprite;
    Sprite bobSprite;
    MyInputProcessor inputProcessor;
    Stage dialogStage;

    CurrentSceneType currentStageType = CurrentSceneType.Dialog;

    @Override
    public void create() {
        batch = new SpriteBatch();

        villageMap = new TmxMapLoader().load("TILESET\\village.tmx");
        guildMap = new TmxMapLoader().load("TILESET\\guild.tmx");
        currentMap = villageMap;

        princess = new Texture("SPRITES\\HEROS\\PRINCESS\\HEROS_PixelPackTOPDOWN8BIT_Princess Idle D.gif");
        bobSprite = new Sprite(new Texture("SPRITES\\HEROS\\ADVENTURER\\HEROS_PixelPackTOPDOWN8BIT_Adventurer Attack D.gif"));
        bobPortrait = new Texture("bob.jpg");

        renderer = new OrthogonalTiledMapRenderer(villageMap, unitScale);

        princessSprite = new Sprite(princess);

        createDialogStage();

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("SKIN//uiskin.atlas"));

    }

    public void createDialogStage() {
        Skin skin = new Skin();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("SKIN//uiskin.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("SKIN\\uiskin.json"));
        Label nameLabel = new Label("Bob, interim's Guild Master", skin);
        dialogStage = new Stage();
        Table table = new Table();
        table.top().left();
        table.add(new Image(bobPortrait)).maxWidth(200).maxHeight(200).left();
        table.row();
        table.add(nameLabel).left();
//        table.add(new Image(bobPortrait));
//        table.addActor(new TextArea("this is  test", TextField.TextFieldStyle));

        table.setFillParent(true);

        dialogStage.addActor(table);

        table.setDebug(true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        if (currentStageType == CurrentSceneType.TiledMap) {
            camera.update();
            renderer.setView(camera);
            renderer.render();

            renderer.getBatch().begin();
            renderer.getBatch().draw(princessSprite, playerCoord.x, playerCoord.y, 1, 1);
            if (currentMap == guildMap) {
                renderer.getBatch().draw(bobSprite, 15, 15, 1, 1);
            }
            renderer.getBatch().end();

            checkCoordinateEvent();
        }

        if (currentStageType == CurrentSceneType.Dialog) {
            dialogStage.act(Gdx.graphics.getDeltaTime());
            dialogStage.draw();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    @Override
    public void resize(int width, int height) {
        if (currentStageType == CurrentSceneType.TiledMap) {
            camera = new OrthographicCamera();
            renderRatio = (float) (width) / (float) height;
            camera.setToOrtho(false, 16 * (renderRatio), 16);

            inputProcessor = new MyInputProcessor(camera, currentMap, playerCoord, renderer);
            Gdx.input.setInputProcessor(inputProcessor);
        } else if (currentStageType == CurrentSceneType.Dialog) {
            dialogStage.getViewport().update(width, height, true);
        }
    }

    public void checkCoordinateEvent() {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) currentMap.getLayers().get(1);

        if (currentMap == villageMap && playerCoord.x == 15 && playerCoord.y == 13) {
            // enter guild
            renderer = new OrthogonalTiledMapRenderer(guildMap, unitScale);
            playerCoord.x = 14;
            playerCoord.y = 1;
            currentMap = guildMap;
            camera = new OrthographicCamera();
            camera.setToOrtho(false, 16 * (renderRatio), 16);
            renderer.setView(camera);
            inputProcessor = new MyInputProcessor(camera, currentMap, playerCoord, renderer);
            Gdx.input.setInputProcessor(inputProcessor);
        }

        if (currentMap == guildMap &&
                (playerCoord.x == 15 || playerCoord.x == 14 || playerCoord.x == 13) &&
                playerCoord.y == 0) {
            // enter village
            renderer = new OrthogonalTiledMapRenderer(villageMap, unitScale);
            playerCoord.x = 15;
            playerCoord.y = 12;
            currentMap = villageMap;
            camera = new OrthographicCamera();
            camera.setToOrtho(false, 16 * (renderRatio), 16);
            renderer.setView(camera);
            inputProcessor = new MyInputProcessor(camera, currentMap, playerCoord, renderer);
            Gdx.input.setInputProcessor(inputProcessor);
        }
    }
}
