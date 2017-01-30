package com.soubhagya.android.game_test;

/**
 * Created by soubhagya on 26/1/17.
 */

public class Player {

    private String mPlayerName;
    private String mTeamId;
    private boolean mIsBase;

    public Player(){}//Default constructer for firebase

    public Player(String name, String teamId, Boolean isBase) {
        mPlayerName = name;
        mTeamId = teamId;
        mIsBase = isBase;
    }

    public String getTeamId() {
        return mTeamId;
    }

    public String getPlayerName() {

        return mPlayerName;
    }

    public boolean isBase() {
        return mIsBase;
    }
}
