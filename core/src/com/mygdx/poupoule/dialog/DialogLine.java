package com.mygdx.poupoule.dialog;

public class DialogLine {
    private final String line;
    private final boolean endOfLine;
    private final boolean endOfDialog;

    public DialogLine(String line, boolean endOfLine, boolean endOfDialog) {
        this.line = line;
        this.endOfLine = endOfLine;
        this.endOfDialog = endOfDialog;
    }

    public String getLine() {
        return line;
    }

    public boolean isEndOfLine() {
        return endOfLine;
    }
}
