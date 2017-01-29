package com.soubhagya.android.game_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by soubhagya on 27/1/17.
 */

public class RegistrationActivity extends AppCompatActivity{

    private SharedPreferences mSharedPreferences;
    private String mPlayerId;
    private Button mCreateButton;
    private Button mJoinButton;

    private DatabaseReference mFirebaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseReference = FirebaseDatabase.getInstance().getReference();

        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREF.FILE, MODE_PRIVATE);

        mPlayerId = mSharedPreferences.getString(Constants.SHARED_PREF.PLAYER_ID,
                Constants.SHARED_PREF.PLAYER_ID_NOT_FOUND);

        if ( mPlayerId.equals(Constants.SHARED_PREF.PLAYER_ID_NOT_FOUND) ) {
            updateUI();
        }
        else {
            //startGame
        }
    }

    private void updateUI() {
        setContentView(R.layout.activity_registration);

        mCreateButton = (Button) findViewById(R.id.create_button);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i=1; i<=10; ++i) {
                   mFirebaseReference.child(Constants.FIREBASE.AVAILABLE_LOCATIONS)
                           .child("Location"+i)
                           .setValue(i*10);
                }

                Intent intent = new Intent(RegistrationActivity.this, CreateTeamActivity.class);
                startActivity(intent);
            }
        });

        mJoinButton = (Button) findViewById(R.id.join_button);
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upcoming
            }
        });
    }
}
