package com.mygdx.poupoule.dialog;

public class PlayerResponseResult {
    String playerResponse;
    String resultDialogName;

    public PlayerResponseResult(String playerResponse, String result) {
        this.playerResponse = playerResponse;
        this.resultDialogName = result;
    }

    public String getPlayerResponse() {
        return playerResponse;
    }

    public String getResultDialogName() {
        return resultDialogName;
    }
}
