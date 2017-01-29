package com.soubhagya.android.game_test;

/**
 * Created by soubhagya on 26/1/17.
 */

public class Player {

    private String mPlayerName;
    private String mTeamId;
    private String mPlayerPhone;
    private boolean mIsBase;

    public String getPlayerPhone() {
        return mPlayerPhone;
    }

    public Player(String name, String teamId, String playerPhone, Boolean isBase) {
        mPlayerName = name;
        mTeamId = teamId;
        mPlayerPhone = playerPhone;
        mIsBase = isBase;
    }

    public String getTeamId() {
        return mTeamId;
    }

    public String getPlayerName() {

        return mPlayerName;
    }

    public void setPlayerName(String playerName) {
        mPlayerName = playerName;
    }

    public boolean isBase() {
        return mIsBase;
    }
}
