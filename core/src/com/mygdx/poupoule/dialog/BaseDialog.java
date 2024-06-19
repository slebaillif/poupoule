package com.mygdx.poupoule.dialog;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mygdx.poupoule.SimpleCoord;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BaseDialog implements GameDialog {
    DialogData data;
    int lineIndex = 0;
    String currentDialog;

    public BaseDialog(String xmlFileToLoad) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        InputStream stream = BaseDialog.class.getClassLoader().getResourceAsStream(xmlFileToLoad);
        data = xmlMapper.readValue(stream, DialogData.class);
        this.currentDialog = data.startingPoint;
    }

    @Override
    public DialogLine getCurrentDialog() {
        boolean endOfLine = lineIndex == data.getExchange(currentDialog).npcSpeech.size() - 1;
        boolean endOfDialog = getPlayerOptions().get(0).nextExchange.equalsIgnoreCase("end");
        return new DialogLine(data.getExchange(currentDialog).npcSpeech.get(lineIndex), endOfLine, endOfDialog);
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
