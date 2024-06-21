package com.mygdx.poupoule.dialog;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mygdx.poupoule.SimpleCoord;
import com.mygdx.poupoule.WorldState;
import com.mygdx.poupoule.combat.Combat;
import com.mygdx.poupoule.combat.MainCharacter;
import com.mygdx.poupoule.inventory.Stackable;
import com.mygdx.poupoule.quest.Condition;
import com.mygdx.poupoule.quest.QuestData;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BaseDialog implements GameDialog {
    DialogData data;
    int lineIndex = 0;
    String currentDialog;
    MainCharacter hero;
    WorldState worldState;

    public BaseDialog(String xmlFileToLoad, MainCharacter hero, WorldState worldState) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        InputStream stream = BaseDialog.class.getClassLoader().getResourceAsStream(xmlFileToLoad);
        data = xmlMapper.readValue(stream, DialogData.class);
        this.currentDialog = data.startingPoint;
        this.hero = hero;
        this.worldState = worldState;
    }

    @Override
    public DialogLine getCurrentDialog() {
        Optional<Exchange> first = data.exchanges.stream().filter(e ->
                e.checkMission != null &&
                        worldState.isQuestActive(e.checkMission) &&
                        checkMission(e.checkMission)
        ).findFirst();
        if (first.isPresent()) {
            currentDialog = first.get().name;
            lineIndex = 0;
            worldState.completeQuest(first.get().checkMission);
        }
        boolean endOfLine = lineIndex == data.getExchange(currentDialog).npcSpeech.size() - 1;
        boolean endOfDialog = getPlayerOptions().get(0).nextExchange.equalsIgnoreCase("end");
        return new DialogLine(data.getExchange(currentDialog).npcSpeech.get(lineIndex), endOfLine, endOfDialog, data.getExchange(currentDialog).giveMission);
    }

    public boolean checkMission(String name) {
        QuestData data = loadQuestData(name);
        boolean fullfilled = true;
        for (Condition condition : data.getFullFillConditions()) {
            List<Stackable> item = hero.getInventory().getQuestThings().stream().filter(q -> q.getName().equals(condition.getItem())).collect(Collectors.toList());
            if (item != null && item.size() == 1) {
                if (item.get(0).getCount() >= condition.getQuantity()) {
                    fullfilled = true;
                }
            } else {
                fullfilled = false;
            }
        }
        return fullfilled;
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

    @Override
    public void moveToNextLine() {
        if (lineIndex == data.getExchange(currentDialog).npcSpeech.size() - 1) {
        } else {
            lineIndex++;
        }
    }

    @Override
    public SimpleCoord getExitCoord() {
        return data.exit;
    }

    @Override
    public List<PlayerResponseResult> getPlayerOptions() {
        return data.getExchange(currentDialog).playerResponses;
    }

    @Override
    public void setCurrentDialog(String dialog) {
        this.currentDialog = dialog;
        this.lineIndex = 0;
    }

    public DialogData getData() {
        return data;
    }
}
