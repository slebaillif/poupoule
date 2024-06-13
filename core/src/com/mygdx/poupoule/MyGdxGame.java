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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mygdx.poupoule.dialog.BaseDialog;
import com.mygdx.poupoule.dialog.DialogInputProcessor;
import com.mygdx.poupoule.dialog.PlayerResponseResult;
import com.mygdx.poupoule.event.EventDetails;
import com.mygdx.poupoule.event.EventType;
import com.mygdx.poupoule.event.GameEvents;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MyGdxGame extends ApplicationAdapter {

    public TiledMap currentMap;
    public String currentMapName = "village";
    float unitScale = 1 / 16f;
    float renderRatio;
    public OrthogonalTiledMapRenderer renderer;
    public OrthographicCamera camera;
    public PlayerCoord playerCoord = new PlayerCoord(13, 15);
    Texture princess;
    Texture rat;
    Sprite princessSprite;
    Sprite bobSprite;
    public TiledMapInputProcessor inputProcessor;
    public Stage dialogStage;
    public Stage combatStage;
    BaseDialog currentDialog;
    public FitViewport dialogViewport;
    public FitViewport combatViewport;
    GameEvents gameMap;
    Map<String, BaseDialog> loadedDialogs = new HashMap<>();

    public CurrentSceneType currentStageType = CurrentSceneType.TiledMap;

    @Override
    public void create() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            InputStream stream = BaseDialog.class.getClassLoader().getResourceAsStream("events\\events.xml");
            gameMap = xmlMapper.readValue(stream, GameEvents.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileName = gameMap.getMapFileName(currentMapName);
        currentMap = loadTileMap(fileName);

        princess = new Texture("SPRITES\\HEROS\\PRINCESS\\HEROS_PixelPackTOPDOWN8BIT_Princess Idle D.gif");
        bobSprite = new Sprite(new Texture("SPRITES\\HEROS\\ADVENTURER\\HEROS_PixelPackTOPDOWN8BIT_Adventurer Attack D.gif"));

        rat = new Texture("SPRITES\\ENEMIES\\rat2.jpg");

        renderer = new OrthogonalTiledMapRenderer(currentMap, unitScale);

        princessSprite = new Sprite(princess);

    }

    TiledMap loadTileMap(String fileName) {
        return new TmxMapLoader().load("TILESET\\" + fileName);
    }

    BaseDialog loadDialog(String name) throws IOException {
        return new BaseDialog("dialogs\\" + name + ".xml");
    }

    public void createDialogStage() {
        Skin skin = new Skin();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("SKIN//uiskin.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("SKIN\\uiskin.json"));
        Label nameLabel = new Label(currentDialog.getData().getTitle(), skin);
        Label dialogLine = new Label(currentDialog.getCurrentDialog().getLine(), skin);
        Label emptyLine = new Label("", skin);


        dialogViewport = new FitViewport(800, 600);
        dialogStage = new Stage(dialogViewport);

        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.drawRectangle(0, 0, 64, 64);
        TextureRegionDrawable borderTexture = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Table table = new Table();
        table.top().left();
        Texture portrait = new Texture(currentDialog.getData().getPortrait());
        table.add(new Image(portrait)).maxWidth(300).maxHeight(300).left();
        table.add(dialogLine).left().expandX().center();
        table.row();
        table.add(nameLabel).center();

        table.row();

        Table table2 = new Table();
        table2.bottom().left().padBottom(150f);
        table2.setBackground(borderTexture);
        if (currentDialog.getCurrentDialog().isEndOfLine()) {
            int i = 1;
            for (PlayerResponseResult response : currentDialog.getPlayerOptions()) {
                Label op = new Label(i + " - " + response.getLine(), skin);
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

    }

    void createCombatStage(){
        Skin skin = new Skin();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("SKIN//uiskin.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("SKIN\\uiskin.json"));
        Label attackLabel = new Label("1 - Attack", skin);
        Label ratAppearLabel = new Label("A rat appears!", skin);
        Label hp = new Label("10 / 10", skin);
        Label hp2 = new Label("10 / 10", skin);
        Label rathp = new Label("Rat A 8 / 8", skin);
        Label rathp2 = new Label("Rat B 8 / 8", skin);
        Label emptyLine = new Label("", skin);


        combatViewport = new FitViewport(850, 600);
        combatStage = new Stage(combatViewport);

        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.drawRectangle(0, 0, 64, 64);
        TextureRegionDrawable borderTexture = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Table table = new Table();
        table.top().left().pad(16);
        Image p = new Image(princessSprite);
        p.scaleBy(5);
        Image ratB = new Image(rat);
        Image ratC = new Image(rat);

        table.row();
        table.add(p).maxWidth(350).maxHeight(100).left().bottom().expandX().expandY();
        table.add(ratB).maxWidth(350).maxHeight(100).right().bottom().expandX().expandY();
        table.row();
        table.add(hp).left();
        table.add(rathp).right();

        table.row();
        table.add(emptyLine);
        table.add(ratC).maxWidth(400).maxHeight(100).right().bottom();
        table.row();
        table.add(emptyLine);
        table.add(rathp2).right();

        table.row().height(200);
        table.add(attackLabel).left();
        table.row().height(100);
        table.add(ratAppearLabel).center().colspan(2);

        Table table2 = new Table();
        table2.setBackground(borderTexture);
        table2.top().left();
        table2.row();
        table2.add(emptyLine).width(800).height(200);

//        table2.bottom().left().padBottom(150f);
//        table2.setBackground(borderTexture);
//        if (currentDialog.getCurrentDialog().isEndOfLine()) {
//            int i = 1;
//            for (PlayerResponseResult response : currentDialog.getPlayerOptions()) {
//                Label op = new Label(i + " - " + response.getLine(), skin);
//                table2.row();
//                table2.add(emptyLine).width(300f);
//                table2.add(op).expandX().center();
//                i++;
//            }
//        } else {
//            Label op = new Label("(Press space bar)", skin);
//            table2.row();
//            table2.add(emptyLine).width(300f);
//            table2.add(op).expandX().center();
//        }

//        Stack stack = new Stack(table, table2);
//        stack.setFillParent(true);

        table.setFillParent(true);
//        table.setDebug(true);
        table2.setFillParent(true);
        combatStage.addActor(table);
        combatStage.addActor(table2);
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
            if (currentMapName.equals("guild")) {
                renderer.getBatch().draw(bobSprite, 15, 15, 1, 1);
            }
            if (currentMapName.equals("healer")) {
                renderer.getBatch().draw(bobSprite, 7, 6, 1, 1);
            }
            renderer.getBatch().end();

            try {
                checkCoordinateEvent();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (currentStageType == CurrentSceneType.Dialog) {
            DialogInputProcessor dialogInputProcessor = new DialogInputProcessor(currentDialog, this);
            Gdx.input.setInputProcessor(dialogInputProcessor);
            createDialogStage();
            dialogStage.act(Gdx.graphics.getDeltaTime());
            dialogStage.draw();
        }
        if(currentStageType == CurrentSceneType.Combat){
            createCombatStage();
            combatStage.act(Gdx.graphics.getDeltaTime());
            combatStage.draw();
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

    public void checkCoordinateEvent() throws IOException {
        EventDetails eventDetails = gameMap.matchLocation(currentMapName, playerCoord.x, playerCoord.y);
        if (eventDetails != null) {
            if (eventDetails.getEventType() == EventType.changeMap) {
                currentMapName = eventDetails.getNewMap();
                currentMap = loadTileMap(gameMap.getMapFileName(currentMapName));
                playerCoord.x = eventDetails.getCoordinates().x;
                playerCoord.y = eventDetails.getCoordinates().y;

                renderer = new OrthogonalTiledMapRenderer(currentMap, unitScale);
                camera = new OrthographicCamera();
                camera.setToOrtho(false, 16 * (renderRatio), 16);
                renderer.setView(camera);
                inputProcessor = new TiledMapInputProcessor(camera, currentMap, playerCoord, renderer);
                Gdx.input.setInputProcessor(inputProcessor);
            }

            if (eventDetails.getEventType() == EventType.dialog) {
                if (loadedDialogs.get(eventDetails.getNewMap()) == null) {
                    loadedDialogs.put(eventDetails.getNewMap(), loadDialog(eventDetails.getNewMap()));
                }
                currentDialog = loadedDialogs.get(eventDetails.getNewMap());
                createDialogStage();
                currentStageType = CurrentSceneType.Dialog;
            }

            if (eventDetails.getEventType() == EventType.combat) {

            }
        }


    }
}
