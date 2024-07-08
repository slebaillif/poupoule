package com.mygdx.poupoule.dialog;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.poupoule.MyGdxGame;
import com.mygdx.poupoule.WorldState;
import com.mygdx.poupoule.quest.QuestData;

public class DialogStage {
    MyGdxGame game;
    float width;
    float height;
    Skin skin;
    Pixmap pixmap;
    BaseDialog currentDialog;
    WorldState worldState;

    public DialogStage(MyGdxGame game, float width, float height, Skin skin, Pixmap pixmap, BaseDialog currentDialog, WorldState worldState) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.skin = skin;
        this.pixmap = pixmap;
        this.currentDialog = currentDialog;
        this.worldState = worldState;
    }

    public Stage createDialogStage() {
        Stage dialogStage = new Stage(new FitViewport(width, height));

        Label nameLabel = new Label(currentDialog.getData().getTitle(), skin);
        DialogLine currentDialog1 = currentDialog.getCurrentDialog();
        Label dialogLine = new Label(currentDialog1.getLine(), skin);
        Label emptyLine = new Label("", skin);

        if (currentDialog1.getGiveMission() != null && !worldState.isQuestActive(currentDialog1.getGiveMission())) {
            QuestData questData = game.loadQuestData(currentDialog1.getGiveMission());
            worldState.addQuest(currentDialog1.getGiveMission(), questData);
        }

        TextureRegionDrawable borderTexture = new TextureRegionDrawable(new Texture(pixmap));

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
                String canAfford = "";
                if (response.getCost() != null) {
                    if (!game.hero.getInventory().canAfford(response.getCost())) {
                        canAfford = " (Cannot afford)";
                    }
                }
                Label op = new Label(i + " - " + response.getLine() + canAfford, skin);
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
        return dialogStage;
    }
}
