package com.mygdx.poupoule.dialog;

import com.mygdx.poupoule.PlayerCoord;

import java.util.List;

public interface GameDialog {
    public DialogLine getCurrentDialog();

    public void moveToNextLine();

    PlayerCoord getExitCoord();

    List<PlayerResponseResult> getPlayerOptions();

    void setCurrentDialog(String dialog);
}
