package com.mygdx.poupoule.dialog;

import com.mygdx.poupoule.SimpleCoord;

import java.util.List;

public interface GameDialog {
    public DialogLine getCurrentDialog();

    public void moveToNextLine();

    SimpleCoord getExitCoord();

    List<PlayerResponseResult> getPlayerOptions();

    void setCurrentDialog(String dialog);
}
