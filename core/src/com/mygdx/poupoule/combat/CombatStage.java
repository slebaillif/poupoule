package com.mygdx.poupoule.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.poupoule.MyGdxGame;
import com.mygdx.poupoule.inventory.Stackable;

import java.util.List;

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
        TextureRegionDrawable borderTexture = new TextureRegionDrawable(new Texture(pixmap));

        Label heroHp = new Label("" + game.hero.getCurrentHitPoints() + " / " + game.hero.getHitPoints(), skin);
        Label emptyLine = new Label("", skin);

        Table table = new Table();
        table.setBackground(borderTexture);
        table.top().left().pad(32);
        Image heroSprite = new Image(this.heroSprite);

        table.row().top().minHeight(100);
        table.add(heroSprite).maxWidth(350).minWidth(100).maxHeight(100).left().bottom().expandX();

        if (combat.getMonsters().get(0).isAlive()) {
            table.add(combat.getMonsters().get(0).getMonsterImage()).maxWidth(350).maxHeight(100).right().bottom().expandX();
        }
        table.row().top();
        table.add(heroHp).left();
        if (combat.getMonsters().get(0).isAlive()) {
            table.add(new Label(combat.getMonsters().get(0).getName() + " = " + combat.getMonsters().get(0).getCurrentHitPoints() + " / " + combat.getMonsters().get(0).getHitPoints(), skin)).right();
        }
        for (int i = 1; i < combat.getMonsters().size(); i++) {
            if (combat.getMonsters().get(i).isAlive()) {
                table.row().top().minHeight(100);
                table.add(emptyLine);
                table.add(combat.getMonsters().get(i).getMonsterImage()).maxWidth(350).maxHeight(100).right().bottom().expandX();
                table.row();
                table.add(emptyLine);
                table.add(new Label(combat.getMonsters().get(1).getName() + " = " + combat.getMonsters().get(i).getCurrentHitPoints() + " / " + combat.getMonsters().get(i).getHitPoints(), skin)).right();
            }
        }

        // ACTIONS - 5 actions max for now
        for (int i = 1; i <= 5; i++) {
            table.row().height(50).top();
            if (i <= combat.getPossibleActions().size()) {
                table.add(new Label("" + i + " - " + combat.getPossibleActions().get(i - 1).getName(), skin)).left();
            } else {
                table.add(emptyLine).left();
            }
        }

        final OpenScrollPane scrollPane = new OpenScrollPane(null, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(false);
        scrollPane.setScrollingDisabled(true, false);
        String lines = String.join("\n", combat.getDisplay());
        final TextArea textArea = new TextArea(lines, skin);

        textArea.setDisabled(true);
        scrollPane.setWidget(textArea);
        table.row().height(100).width(300).top();
        table.add(scrollPane).right().colspan(2).width(580).height(170);

        table.setFillParent(true);
        combatStage.addActor(table);
        Gdx.input.setInputProcessor(combat);
        table.setDebug(true);
        return combatStage;
    }

    public class OpenScrollPane extends ScrollPane {

        private boolean scrollToBottom;

        public OpenScrollPane(Actor widget, Skin skin) {
            super(widget, skin);
        }

        public void scheduleScrollToBottom() {
            scrollToBottom = true;
        }

        @Override
        public void layout() {
            super.layout();
            if (scrollToBottom) {
                scrollTo(0,0,0,0);

            }
        }

    }
}
