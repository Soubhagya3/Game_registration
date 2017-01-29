package com.soubhagya.android.game_test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

/**
 * Created by soubhagya on 28/1/17.
 */

public class CreateTeamActivity extends AppCompatActivity{
    private EditText mTeamNameEditText;
    private EditText mPlayerNameEditText;
    private EditText mPasswordEditText;
    private Button mSubmitButton;

    private String mTeamName;
    private String mPlayerName;
    private String mPassword;

    private String mTeamLocation;

    private DatabaseReference mFirebaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        mFirebaseReference = FirebaseDatabase.getInstance().getReference();

        mTeamNameEditText = (EditText) findViewById(R.id.team_name_edit_text);
        mTeamNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTeamName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPlayerNameEditText = (EditText) findViewById(R.id.player_name_edit_text);
        mPlayerNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPlayerName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPassword = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTeamName!=null && mPlayerName!=null && mPassword!=null) {
                    mFirebaseReference.child(Constants.FIREBASE.AVAILABLE_LOCATIONS)
                            .runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {
                                    MutableData firstNode = mutableData.getChildren().iterator().next();

                                    if (firstNode != null) {
                                        mTeamLocation = firstNode.getKey();
                                        firstNode.setValue(null);
                                        return Transaction.success(mutableData);
                                    }
                                    else {
                                        return Transaction.success(mutableData);
                                    }
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                    if (databaseError != null) {
                                        Log.d("TRANSACTION", "Database error occurred");
                                        Toast.makeText(getApplicationContext(),
                                                "Database error occurred!",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                    else {
                                        createTeam(mTeamLocation);//All the important work is here!
                                        Log.d("TRANSACTION", "Team Created!");
                                        Toast.makeText(getApplicationContext(),
                                                "Team Created!",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Please fill all details",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void createTeam(String teamLocation) {

        String teamId = addInGeoFire(teamLocation);

        createBase(teamId);//create Dummy Player

        createPlayer(teamId);//create Player

        Team newTeam = new Team(mTeamName);

        mFirebaseReference.child(Constants.FIREBASE.TEAMS)
                            .child(teamId)
                            .setValue(newTeam);
    }

    private void createBase (String teamId) {
        Player newBase = new Player(mTeamName, teamId, "dummyPassword", true);

        mFirebaseReference.child(Constants.FIREBASE.PLAYERS)
                .child(teamId)
                .setValue(newBase);
    }

    private void createPlayer(String teamId) {
        DatabaseReference node = mFirebaseReference.child(Constants.FIREBASE.PLAYERS).push();

        Player newPlayer = new Player(mPlayerName, teamId, mPassword, false);

        node.setValue(newPlayer);
    }

    private String addInGeoFire(String teamLocation) {
        DatabaseReference node = mFirebaseReference.child(Constants.FIREBASE.GEOFIRE).push();

        node.setValue(teamLocation);

        return node.getKey();
    }
}
