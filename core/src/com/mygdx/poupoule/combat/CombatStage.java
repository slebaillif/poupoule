package com.mygdx.poupoule.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.poupoule.MyGdxGame;
import com.mygdx.poupoule.inventory.Stackable;

public class CombatStage {
    MyGdxGame game;
    Combat combat;
    Skin skin;
    Pixmap pixmap;
    float width;
    float height;
    Sprite heroSprite;

    public CombatStage(MyGdxGame game, Combat combat, Skin skin, Pixmap pixmap, float width, float height, Sprite heroSprite) {
        this.game = game;
        this.combat = combat;
        this.skin = skin;
        this.pixmap = pixmap;
        this.width = width;
        this.height = height;
        this.heroSprite = heroSprite;
    }

    public Stage createCombatStage() {
        Stage combatStage = new Stage(new FitViewport(width, height));

        Label ratAppearLabel = new Label(combat.getDisplay(), skin);
        Label hp = new Label("" + game.hero.getCurrentHitPoints() + " / " + game.hero.getHitPoints(), skin);
        Label emptyLine = new Label("", skin);

        Table table = new Table();
        table.top().left().pad(64);
        Image p = new Image(heroSprite);
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
        TextureRegionDrawable borderTexture = new TextureRegionDrawable(new Texture(pixmap));
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
        return combatStage;
    }
}
