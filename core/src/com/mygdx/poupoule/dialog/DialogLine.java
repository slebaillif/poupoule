package com.mygdx.poupoule.dialog;

public class DialogLine {
    private final String line;
    private final boolean endOfLine;
    private final boolean endOfDialog;
    private final String giveMission;

    public DialogLine(String line, boolean endOfLine, boolean endOfDialog, String giveMission) {
        this.line = line;
        this.endOfLine = endOfLine;
        this.endOfDialog = endOfDialog;
        this.giveMission = giveMission;
    }

    public String getLine() {
        return line;
    }

    public boolean isEndOfLine() {
        return endOfLine;
    }

    public String getGiveMission() {
        return giveMission;
    }
}
