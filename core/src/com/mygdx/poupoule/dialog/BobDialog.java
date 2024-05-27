package com.mygdx.poupoule.dialog;

import com.mygdx.poupoule.PlayerCoord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class BobDialog implements GameDialog {
    Map<String, List<String>> dialogLines = new HashMap<String, List<String>>();
    Map<String, List<PlayerResponseResult>> playerResponseLines = new HashMap<String, List<PlayerResponseResult>>();
    String currentDialog = "firstMeeting";
    int lineIndex = 0;

    public BobDialog() {
        dialogLines.put("firstMeeting",
                asList("Hi there, my name is Bob. ",
                        "I run this place, for now.",
                        "If you expect me to be all sunshine and rainbows, \nyou’re very wrong noobs.",
                        "Plus it’s Monday morning."));

        playerResponseLines.put("firstMeeting",
                asList(
                        new PlayerResponseResult("So grumpy, so young.", "watchIt"),
                        new PlayerResponseResult("Can we skip to the part where you give us a mission?", "notInGuild")));

        dialogLines.put("watchIt", asList("Hey, watch your mouth!"));
        playerResponseLines.put("watchIt", asList(new PlayerResponseResult("Can we skip to the part where you give us a mission?", "notInGuild")));

        dialogLines.put("notInGuild", asList("You're not registered with the Guild, \nso I can't give you any mission, you see."));
        playerResponseLines.put("notInGuild", asList(new PlayerResponseResult("How can we register ?", "register")));

        dialogLines.put("register", asList("Just kill 10 rats and \nbring me their entrails."));
        playerResponseLines.put("register", asList(new PlayerResponseResult("I\'m really looking forward to it.", "end")));
    }

    @Override
    public DialogLine getCurrentDialog() {
        boolean endOfLine = lineIndex == dialogLines.get(currentDialog).size() - 1;
        boolean endOfDialog = getPlayerOptions().get(0).resultDialogName.equalsIgnoreCase("end");
        return new DialogLine(dialogLines.get(currentDialog).get(lineIndex), endOfLine, endOfDialog);

    }

    @Override
    public void moveToNextLine() {
        if (lineIndex == dialogLines.get(currentDialog).size() - 1) {
        } else {
            lineIndex++;
        }
    }

    @Override
    public PlayerCoord getExitCoord() {
        return new PlayerCoord(15, 12);
    }

    @Override
    public List<PlayerResponseResult> getPlayerOptions() {
        return playerResponseLines.get(currentDialog);
    }

    @Override
    public void setCurrentDialog(String dialog) {
        this.currentDialog = dialog;
        this.lineIndex = 0;
    }


}
