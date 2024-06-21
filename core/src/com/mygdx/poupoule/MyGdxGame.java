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
import com.mygdx.poupoule.combat.*;
import com.mygdx.poupoule.dialog.BaseDialog;
import com.mygdx.poupoule.dialog.DialogInputProcessor;
import com.mygdx.poupoule.dialog.DialogLine;
import com.mygdx.poupoule.dialog.PlayerResponseResult;
import com.mygdx.poupoule.event.EventDetails;
import com.mygdx.poupoule.event.EventType;
import com.mygdx.poupoule.event.GameEvents;
import com.mygdx.poupoule.event.GameLocation;
import com.mygdx.poupoule.inventory.Stackable;
import com.mygdx.poupoule.quest.QuestData;

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
    public FitViewport dialogViewport;
    public FitViewport combatViewport;
    GameEvents gameMap;
    Map<String, BaseDialog> loadedDialogs = new HashMap<>();

    public CurrentSceneType currentStageType = CurrentSceneType.TiledMap;
    HashMap<String, Sprite> npcSprites = new HashMap<>(10);
    Combat combat;
    MainCharacter hero;
    WorldState worldState = new WorldState();
    private Stage inventoryStage;

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

        String fileName = gameMap.getMapFileName(currentMapName);
        currentMap = loadTileMap(fileName);

        princess = new Texture("SPRITES\\HEROS\\PRINCESS\\HEROS_PixelPackTOPDOWN8BIT_Princess Idle D.gif");
        Sprite daphne = new Sprite(new Texture("SPRITES\\daphne.PNG"));
        Sprite bobSprite = new Sprite(new Texture("SPRITES\\HEROS\\ADVENTURER\\HEROS_PixelPackTOPDOWN8BIT_Adventurer Attack D.gif"));

        npcSprites.put("bob", bobSprite);
        npcSprites.put("daphne", daphne);

        renderer = new OrthogonalTiledMapRenderer(currentMap, unitScale);
        hero = new MainCharacter("PrinSeSS", 3, 0, 15);
        princessSprite = new Sprite(princess);
    }

    public void changeSceneType(CurrentSceneType sceneType) {
        if (sceneType == CurrentSceneType.TiledMap) {
            screenForMap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

    QuestData loadQuestData(String questName) {
        XmlMapper xmlMapper = new XmlMapper();
        InputStream stream = Combat.class.getClassLoader().getResourceAsStream("events\\" + questName + ".xml");
        try {
            return xmlMapper.readValue(stream, QuestData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createDialogStage() {
        Skin skin = new Skin();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("SKIN//uiskin.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("SKIN\\uiskin.json"));
        Label nameLabel = new Label(currentDialog.getData().getTitle(), skin);
        DialogLine currentDialog1 = currentDialog.getCurrentDialog();
        Label dialogLine = new Label(currentDialog1.getLine(), skin);
        Label emptyLine = new Label("", skin);

        if (currentDialog1.getGiveMission() != null && !worldState.isQuestActive(currentDialog1.getGiveMission())) {
            QuestData questData = loadQuestData(currentDialog1.getGiveMission());
            worldState.addQuest(currentDialog1.getGiveMission(), questData);
        }

        dialogViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        if (currentDialog1.isEndOfLine()) {
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

    void createCombatStage() {
        Skin skin = new Skin();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("SKIN//uiskin.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("SKIN\\uiskin.json"));

        combatViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        combatStage = new Stage(combatViewport);

        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.drawRectangle(0, 0, 64, 64);
        TextureRegionDrawable borderTexture = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();


        Label ratAppearLabel = new Label(combat.getDisplay(), skin);
        Label hp = new Label("" + hero.getCurrentHitPoints() + " / " + hero.getHitPoints(), skin);
        Label emptyLine = new Label("", skin);

        Table table = new Table();
        table.top().left().pad(64);
        Image p = new Image(princessSprite);
        p.scaleBy(5);


        table.row();
        table.add(p).maxWidth(350).maxHeight(100).left().bottom().expandX().expandY();

        if (combat.getMonsters().get(0).isAlive()) {
            table.add(combat.getMonsters().get(0).getMonsterImage()).maxWidth(350).maxHeight(100).right().bottom().expandX().expandY();
        }
        table.row();
        table.add(hp).left();
        if (combat.getMonsters().get(0).isAlive()) {
            table.add(new Label(combat.getMonsters().get(0).getName() + " = " + combat.getMonsters().get(0).getCurrentHitPoints() + " / " + combat.getMonsters().get(0).getHitPoints(), skin)).right();
        }
        table.row();
        for (int i = 1; i < combat.getMonsters().size(); i++) {
            if (combat.getMonsters().get(i).isAlive()) {
                table.add(emptyLine);
                table.add(combat.getMonsters().get(i).getMonsterImage()).maxWidth(400).maxHeight(100).right().bottom();
                table.row();
                table.add(emptyLine);
                table.add(new Label(combat.getMonsters().get(1).getName() + " = " + combat.getMonsters().get(i).getCurrentHitPoints() + " / " + combat.getMonsters().get(i).getHitPoints(), skin)).right();
            }
        }

        table.row().height(100);
        for (int i = 1; i <= combat.getPossibleActions().size(); i++) {
            table.row().height(100);
            table.add(new Label("" + i + " - " + combat.getPossibleActions().get(i - 1).getName(), skin)).left();
        }
        table.row().height(200);
        table.add(ratAppearLabel).center().colspan(2);
        if (combat.allMonstersDefeated()) {
            table.row();
            table.add(new Label("LOOT", skin)).center().colspan(2);
            for (Stackable s : combat.getLoot()) {
                table.row();
                table.add(new Label(s.getName() + "(" + s.getCount() + ")", skin));
            }
        }

        Table table2 = new Table();
        table2.setBackground(borderTexture);
        table2.top().left();
        table2.row();
        table2.add(emptyLine).width(800).height(200);

        table.setFillParent(true);
        table2.setFillParent(true);
        combatStage.addActor(table);
        combatStage.addActor(table2);
        Gdx.input.setInputProcessor(combat);
    }

    public void createInventoryStage() {
        Skin skin = new Skin();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("SKIN//uiskin.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("SKIN\\uiskin.json"));
        Label nameLabel = new Label("INVENTORY", skin);
        Label emptyLine = new Label("", skin);


        dialogViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        inventoryStage = new Stage(dialogViewport);

        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.drawRectangle(0, 0, 64, 64);
        TextureRegionDrawable borderTexture = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Table table = new Table();
        table.top().left().pad(128).padTop(256);
        Image p = new Image(princessSprite);
        p.scaleBy(10);
        table.add(p).maxWidth(300).maxHeight(300).left();
        table.row();
        //attack
        table.row();
        table.add(new Label("Hit Points: " + hero.getCurrentHitPoints() + " / " + hero.getHitPoints(), skin)).left();
        table.add(new Label("", skin)).left();
        table.row();
        table.add(new Label("Attack: " + hero.getAttack(), skin)).left();
        table.add(new Label("Weapon: " + hero.getInventory().getWeapon(), skin)).left();
        table.row();
        table.add(new Label("Defense: " + hero.getDefense(), skin)).left();
        table.add(new Label("Armor: " + hero.getInventory().getArmor(), skin)).left();

        table.row().padTop(64);
        table.add(new Label("Quest Items", skin)).left();
        for (Stackable item : hero.getInventory().getQuestThings()) {
            table.row();
            table.add(new Label("- " + item.getName() + "(" + item.getCount() + ")", skin)).left();
        }

        table.row().padTop(64);
        table.add(new Label("Quest(s)", skin)).left();
        for (QuestData item : worldState.activeQuests.values()) {
            table.row();
            table.add(new Label("- " + item.getMissionName(), skin)).left();
        }


        Table table2 = new Table();
        table2.bottom().left().padBottom(150f);
        table2.setBackground(borderTexture);

        Stack stack = new Stack(table, table2);
        stack.setFillParent(true);

        inventoryStage.addActor(stack);

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
    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {
        if (currentStageType == CurrentSceneType.TiledMap) {
            screenForMap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else if (currentStageType == CurrentSceneType.Dialog) {
            dialogStage.getViewport().update(width, height, true);
        }
    }

    private void screenForMap(int width, float height) {
        renderRatio = (float) (width) / (float) height;
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

                screenForMap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

