package com.soubhagya.android.game_test;

import java.util.ArrayList;

/**
 * Created by soubhagya on 27/1/17.
 */

public class Team {

    private String mTeamName;
    private int mPosLeft;

    private ArrayList<String> mPlayerList;

    public Team(){}//Default constructer for firebase

    public Team(String teamName) {
        mTeamName = teamName;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public int getPosLeft() {
        return mPosLeft;
    }

    public ArrayList<String> getPlayerList() {
        return mPlayerList;
    }
}
