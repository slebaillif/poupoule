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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mygdx.poupoule.combat.*;
import com.mygdx.poupoule.dialog.BaseDialog;
import com.mygdx.poupoule.dialog.DialogInputProcessor;
import com.mygdx.poupoule.dialog.DialogStage;
import com.mygdx.poupoule.event.EventDetails;
import com.mygdx.poupoule.event.EventType;
import com.mygdx.poupoule.event.GameEvents;
import com.mygdx.poupoule.event.GameLocation;
import com.mygdx.poupoule.inventory.InventoryStage;
import com.mygdx.poupoule.quest.QuestData;
import com.mygdx.poupoule.splash.SplashScreen;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.mygdx.poupoule.combat.TargetType.singleHero;
import static com.mygdx.poupoule.combat.TargetType.singleMonster;

public class MyGdxGame extends ApplicationAdapter {

    public TiledMap currentMap;
    public String currentMapName = "village";
    float unitScale = 1 / 16f;
    float renderRatio;
    public OrthogonalTiledMapRenderer renderer;
    public OrthographicCamera camera;
    public SimpleCoord playerCoord = new SimpleCoord(13, 10);
    Texture princess;
    Sprite princessSprite;
    public TiledMapInputProcessor inputProcessor;
    public Stage dialogStage;
    public Stage combatStage;
    BaseDialog currentDialog;
    GameEvents gameMap;
    Map<String, BaseDialog> loadedDialogs = new HashMap<>();

    public CurrentSceneType currentStageType = CurrentSceneType.SplashScreen;
    HashMap<String, Sprite> npcSprites = new HashMap<>(10);
    Combat combat;
    public MainCharacter hero;
    WorldState worldState = new WorldState();
    private Stage inventoryStage;
    public Skin skin;
    public Pixmap pixmap;
    Stage splashScreenStage;

    public WorldState getWorldState() {
        return worldState;
    }

    @Override
    public void create() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            InputStream stream = BaseDialog.class.getClassLoader().getResourceAsStream("events\\events.xml");
            gameMap = xmlMapper.readValue(stream, GameEvents.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        skin = new Skin();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("SKIN//uiskin.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("SKIN\\uiskin.json"));

        pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.drawRectangle(0, 0, 64, 64);

        String fileName = gameMap.getMapFileName(currentMapName);
        currentMap = loadTileMap(fileName);

        princess = new Texture("SPRITES\\HEROS\\PRINCESS\\HEROS_PixelPackTOPDOWN8BIT_Princess Idle D.gif");
        Sprite daphne = new Sprite(new Texture("SPRITES\\daphne.PNG"));
        Sprite bobSprite = new Sprite(new Texture("SPRITES\\HEROS\\ADVENTURER\\HEROS_PixelPackTOPDOWN8BIT_Adventurer Attack D.gif"));

        npcSprites.put("bob", bobSprite);
        npcSprites.put("daphne", daphne);

        renderer = new OrthogonalTiledMapRenderer(currentMap, unitScale);
        hero = new MainCharacter("PrinSeSS", 3, 0, 15);
        hero.isHit(5);
        princessSprite = new Sprite(princess);

        splashScreenStage = new SplashScreen(skin, this, getForcedWidth(), getForcedHeight()).createStage();
    }

    public void changeSceneType(CurrentSceneType sceneType) {
        if (sceneType == CurrentSceneType.TiledMap) {
            screenForMap(getForcedWidth(), getForcedHeight());
        }
        this.currentStageType = sceneType;
    }

    TiledMap loadTileMap(String fileName) {
        return new TmxMapLoader().load("TILESET\\" + fileName);
    }

    BaseDialog loadDialog(String name) throws IOException {
        return new BaseDialog("dialogs\\" + name + ".xml", hero, worldState);
    }

    Combat loadCombat(String name) throws IOException {
        return new Combat("combats\\" + name + ".xml");
    }

    public QuestData loadQuestData(String questName) {
        XmlMapper xmlMapper = new XmlMapper();
        InputStream stream = Combat.class.getClassLoader().getResourceAsStream("events\\" + questName + ".xml");
        try {
            return xmlMapper.readValue(stream, QuestData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    float getForcedWidth() {
        return 1280;
    }

    float getForcedHeight() {
        return 720;
    }

    public void createDialogStage() {
        dialogStage = new DialogStage(this, getForcedWidth(), getForcedHeight(), skin, pixmap, currentDialog, worldState).createDialogStage();
    }

    void createCombatStage() {
        combatStage = new CombatStage(this, combat, skin, pixmap, getForcedWidth(), getForcedHeight(), princessSprite).createCombatStage();
    }

    public void createInventoryStage() {
        inventoryStage = new InventoryStage(this, getForcedWidth(), getForcedHeight(), princessSprite, pixmap, skin, worldState).createInventoryStage();
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

            java.util.List<GameLocation> npcs = gameMap.getEventFromType(currentMapName, EventType.npc);
            for (GameLocation npc : npcs) {
                EventDetails d = npc.getEventDetails();
                renderer.getBatch().draw(npcSprites.get(d.getNewMap()), npc.getCoordinates().x, npc.getCoordinates().y, 1, 1);
            }
            java.util.List<GameLocation> combats = gameMap.getEventFromType(currentMapName, EventType.combat);
            for (GameLocation cb : combats) {
                EventDetails d = cb.getEventDetails();
                if (!worldState.isCombatResolved(currentMapName, d.getNewMap())) {
                    Sprite cbImage = new Sprite(new Texture("SPRITES\\ENEMIES\\rat2.jpg"));
                    renderer.getBatch().draw(cbImage, cb.getCoordinates().x, cb.getCoordinates().y, 1, 1);
                }
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
        if (currentStageType == CurrentSceneType.Combat) {
            createCombatStage();
            combatStage.act(Gdx.graphics.getDeltaTime());
            combatStage.draw();
        }
        if (currentStageType == CurrentSceneType.Inventory) {
            createInventoryStage();
            inventoryStage.act(Gdx.graphics.getDeltaTime());
            inventoryStage.draw();
        }
        if (currentStageType == CurrentSceneType.SplashScreen) {
            splashScreenStage.act(Gdx.graphics.getDeltaTime());
            splashScreenStage.draw();
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {
        if (currentStageType == CurrentSceneType.TiledMap) {
            screenForMap(getForcedWidth(), getForcedHeight());
        } else if (currentStageType == CurrentSceneType.Dialog) {
            dialogStage.getViewport().update(width, height, true);
        }
    }

    private void screenForMap(float width, float height) {
        renderRatio = width / height;
        renderer = new OrthogonalTiledMapRenderer(currentMap, unitScale);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16 * (renderRatio), 16);
        renderer.setView(camera);
        inputProcessor = new TiledMapInputProcessor(camera, currentMap, playerCoord, renderer, this);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    public void checkCoordinateEvent() throws IOException {
        EventDetails eventDetails = gameMap.matchLocation(currentMapName, playerCoord.x, playerCoord.y);
        if (eventDetails != null) {
            if (eventDetails.getEventType() == EventType.changeMap) {
                currentMapName = eventDetails.getNewMap();
                currentMap = loadTileMap(gameMap.getMapFileName(currentMapName));
                playerCoord.x = eventDetails.getCoordinates().x;
                playerCoord.y = eventDetails.getCoordinates().y;

                screenForMap(getForcedWidth(), getForcedHeight());
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
                if (!worldState.isCombatResolved(currentMapName, eventDetails.getNewMap())) {
                    this.combat = loadCombat(eventDetails.getNewMap());

                    PlayerAction attack = new PlayerAction("Attack", singleMonster, new DamageEffect(3));
                    PlayerAction leave = new ExitAction("Leave combat", singleHero, null);
                    combat.addActions(Arrays.asList(attack, leave));
                    combat.setHero(hero);
                    combat.setTheGame(this);
                    this.changeSceneType(CurrentSceneType.Combat);
                }
            }
        }
    }
}

