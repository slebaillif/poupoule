package com.mygdx.poupoule.dialog;

import com.mygdx.poupoule.PlayerCoord;

public interface GameDialog {
    public String getCurrentDialog();
    public void moveToNextLine();
    PlayerCoord getExitCoord();
}
