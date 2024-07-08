package com.mygdx.poupoule.inventory;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.poupoule.MyGdxGame;
import com.mygdx.poupoule.WorldState;
import com.mygdx.poupoule.quest.QuestData;

public class InventoryStage {
    MyGdxGame game;
    float width;
    float height;
    Sprite heroSprite;
    Pixmap pixmap;
    Skin skin;
    WorldState worldState;

    public InventoryStage(MyGdxGame game, float width, float height, Sprite heroSprite, Pixmap pixmap, Skin skin, WorldState worldState) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.heroSprite = heroSprite;
        this.pixmap = pixmap;
        this.skin = skin;
        this.worldState = worldState;
    }

    public Stage createInventoryStage() {
        Stage inventoryStage = new Stage(new FitViewport(width, height));

        TextureRegionDrawable borderTexture = new TextureRegionDrawable(new Texture(pixmap));

        Table table = new Table();
        table.top().left().pad(128).padTop(256);
        Image p = new Image(heroSprite);
        p.scaleBy(10);
        table.add(p).maxWidth(300).maxHeight(300).left();
        table.row();
        //attack
        table.row();
        table.add(new Label("Hit Points: " + game.hero.getCurrentHitPoints() + " / " + game.hero.getHitPoints(), skin)).left();
        table.add(new Label("", skin)).left();
        table.row();
        table.add(new Label("Attack: " + game.hero.getAttack(), skin)).left();
        table.add(new Label("Weapon: " + game.hero.getInventory().getWeapon(), skin)).left();
        table.row();
        table.add(new Label("Defense: " + game.hero.getDefense(), skin)).left();
        table.add(new Label("Armor: " + game.hero.getInventory().getArmor(), skin)).left();

        table.row().padTop(64);
        table.add(new Label("Quest Items", skin)).left();
        for (Stackable item : game.hero.getInventory().getQuestThings()) {
            table.row();
            table.add(new Label("- " + item.getName() + "(" + item.getCount() + ")", skin)).left();
        }

        table.row().padTop(64);
        table.add(new Label("Quest(s)", skin)).left();
        for (QuestData item : worldState.getActiveQuests().values()) {
            table.row();
            table.add(new Label("- " + item.getMissionName(), skin)).left();
        }


        Table table2 = new Table();
        table2.bottom().left().padBottom(150f);
        table2.setBackground(borderTexture);

        Stack stack = new Stack(table, table2);
        stack.setFillParent(true);

        inventoryStage.addActor(stack);
        return inventoryStage;
    }
}
