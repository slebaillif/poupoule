package com.mygdx.poupoule.dialog;

import com.mygdx.poupoule.PlayerCoord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class BobDialog implements GameDialog {
    Map<String, List<String>> dialogLines = new HashMap<String, List<String>>();
    String currentDialog = "firstMeeting";
    int lineIndex = 0;

    public BobDialog() {
        dialogLines.put("firstMeeting",
                asList("Hi there, my name is Bob. ",
                        "I run this place, for now.",
                        "If you expect me to be all sunshine and rainbows, \nyou’re very wrong noobs.",
                        "Plus it’s Monday morning."));
    }

    @Override
    public String getCurrentDialog() {
        if (lineIndex > dialogLines.get(currentDialog).size() - 1) {
            return "END";
        } else {
            return dialogLines.get(currentDialog).get(lineIndex);
        }
    }

    @Override
    public void moveToNextLine() {
        lineIndex++;
    }

    @Override
    public PlayerCoord getExitCoord() {
        return new PlayerCoord(15, 12);
    }


}
