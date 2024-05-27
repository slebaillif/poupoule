package com.mygdx.poupoule;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.poupoule.dialog.BobDialog;
import com.mygdx.poupoule.dialog.DialogInputProcessor;
import com.mygdx.poupoule.dialog.PlayerResponseResult;

public class MyGdxGame extends ApplicationAdapter {

    public TiledMap currentMap;
    TiledMap villageMap;
    TiledMap guildMap;
    float unitScale = 1 / 16f;
    float renderRatio;
    public OrthogonalTiledMapRenderer renderer;
    public OrthographicCamera camera;
    public PlayerCoord playerCoord = new PlayerCoord(13, 15);
    Texture princess;
    Texture bobPortrait;
    Sprite princessSprite;
    Sprite bobSprite;
    public TiledMapInputProcessor inputProcessor;
    public Stage dialogStage;
    BobDialog bobDialog = new BobDialog();
    public FitViewport dialogViewport;

    public CurrentSceneType currentStageType = CurrentSceneType.TiledMap;

    @Override
    public void create() {

        villageMap = new TmxMapLoader().load("TILESET\\village.tmx");
        guildMap = new TmxMapLoader().load("TILESET\\guild.tmx");
        currentMap = villageMap;

        princess = new Texture("SPRITES\\HEROS\\PRINCESS\\HEROS_PixelPackTOPDOWN8BIT_Princess Idle D.gif");
        bobSprite = new Sprite(new Texture("SPRITES\\HEROS\\ADVENTURER\\HEROS_PixelPackTOPDOWN8BIT_Adventurer Attack D.gif"));
        bobPortrait = new Texture("bob.jpg");

        renderer = new OrthogonalTiledMapRenderer(villageMap, unitScale);

        princessSprite = new Sprite(princess);

        createDialogStage();

    }

    public void createDialogStage() {
        Skin skin = new Skin();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("SKIN//uiskin.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("SKIN\\uiskin.json"));
        Label nameLabel = new Label("Bob, interim's Guild Master", skin);
        Label dialogLine = new Label(bobDialog.getCurrentDialog().getLine(), skin);
        Label emptyLine = new Label("", skin);


        dialogViewport = new FitViewport(800, 600);
        dialogStage = new Stage(dialogViewport);

        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.drawRectangle(0, 0, 64, 64);
        TextureRegionDrawable borderTexture = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Table table = new Table();
//        table.setBackground(borderTexture);
        table.top().left();
        table.add(new Image(bobPortrait)).maxWidth(300).maxHeight(300).left();
            table.add(dialogLine).left().expandX().center();
        table.row();
        table.add(nameLabel).center();

        table.row();

        Table table2 = new Table();
        table2.bottom().left().padBottom(150f);
        table2.setBackground(borderTexture);
        if (bobDialog.getCurrentDialog().isEndOfLine()) {
            int i = 1;
            for (PlayerResponseResult response : bobDialog.getPlayerOptions()) {
                Label op = new Label(i + " - " + response.getPlayerResponse(), skin);
                table2.row();
                table2.add(emptyLine).width(300f);
                table2.add(op).expandX().center();
                i++;
            }
        } else {
            Label op = new Label("(Press space bar)", skin);
            table2.row();
            table2.add(emptyLine).width(300f);
            table2.add(op).expandX().center();
        }

        Stack stack = new Stack(table, table2);
        stack.setFillParent(true);

        dialogStage.addActor(stack);

//        table.setDebug(true);
        table2.setDebug(true);
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
            DialogInputProcessor dialogInputProcessor = new DialogInputProcessor(bobDialog, this);
            Gdx.input.setInputProcessor(dialogInputProcessor);
            createDialogStage();
            dialogStage.act(Gdx.graphics.getDeltaTime());
            dialogStage.draw();
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {
        if (currentStageType == CurrentSceneType.TiledMap) {
            camera = new OrthographicCamera();
            renderRatio = (float) (width) / (float) height;
            camera.setToOrtho(false, 16 * (renderRatio), 16);

            inputProcessor = new TiledMapInputProcessor(camera, currentMap, playerCoord, renderer);
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
            inputProcessor = new TiledMapInputProcessor(camera, currentMap, playerCoord, renderer);
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
            inputProcessor = new TiledMapInputProcessor(camera, currentMap, playerCoord, renderer);
            Gdx.input.setInputProcessor(inputProcessor);
        }

        if (currentMap == guildMap &&
                playerCoord.x == 15 &&
                playerCoord.y == 13) {
            // enter bob dialog
            currentStageType = CurrentSceneType.Dialog;
        }
    }
}
