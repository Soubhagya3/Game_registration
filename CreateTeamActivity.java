package com.soubhagya.android.game_test;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;

/**
 * Created by soubhagya on 28/1/17.
 */

public class CreateTeamActivity extends AppCompatActivity{

    public static Intent newIntent(Context context, boolean teamRegistered, String teamName, String phone) {
        Intent intent = new Intent(context, CreateTeamActivity.class);
        intent.putExtra(Constants.EXTRAS.TEAM_REGISTERED, teamRegistered);
        intent.putExtra(Constants.EXTRAS.TEAMNAME, teamName);
        intent.putExtra(Constants.EXTRAS.PHONE, phone);
        return intent;
    }

    private String mTeamName;
    private String mPhone;
    private String mPlayerName;
    private String mCharacterNumber;

    private boolean mTeamRegistered;
    private String mTeamLocation;

    private DatabaseReference mFirebaseReference;

    //UI components
    private EditText mPlayerNameEditText;
    private EditText mCharacterNumberEditText;
    private Button mSubmitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        mFirebaseReference = FirebaseDatabase.getInstance().getReference();

        getAllExtras();

        mPlayerNameEditText = (EditText) findViewById(R.id.playerName_editText);
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

        mCharacterNumberEditText = (EditText) findViewById(R.id.character_number_editText);
        mCharacterNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCharacterNumber = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (mPlayerName != null && mCharacterNumber != null) {
                        mFirebaseReference.child(Constants.FIREBASE.AVAILABLE_LOCATIONS)
                                .runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        MutableData firstNode = mutableData.getChildren().iterator().next();

                                        if (firstNode != null) {
                                            mTeamLocation = firstNode.getKey();
                                            firstNode.setValue(null);
                                            return Transaction.success(mutableData);
                                        } else {
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
                                        } else {
                                            createTeam(mTeamLocation);//All the important work is here!
                                            Log.d("TRANSACTION", "Team Created!");
                                            Toast.makeText(getApplicationContext(),
                                                    "Team Created!",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                            //START GAME
                                        }
                                    }
                                });
                    } else {
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
        Player newBase = new Player(mTeamName, teamId, true);

        mFirebaseReference.child(Constants.FIREBASE.PLAYERS)
                .child(teamId)
                .setValue(newBase);
    }

    private void createPlayer(String teamId) {
        DatabaseReference node = mFirebaseReference.child(Constants.FIREBASE.PLAYERS).push();

        Player newPlayer = new Player(mPlayerName, teamId, false);

        node.setValue(newPlayer);

        mFirebaseReference.child(Constants.FIREBASE.PLAYERS).child(node.getKey())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Player x = dataSnapshot.getValue(Player.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private String addInGeoFire(String teamLocation) {
        DatabaseReference node = mFirebaseReference.child(Constants.FIREBASE.GEOFIRE).push();

        node.setValue(teamLocation);

        return node.getKey();
    }

    private void getAllExtras() {
        Intent intent = getIntent();
        mTeamName = intent.getSerializableExtra(Constants.EXTRAS.TEAMNAME).toString();
        mPhone = intent.getSerializableExtra(Constants.EXTRAS.PHONE).toString();
        mTeamRegistered = intent.getBooleanExtra(Constants.EXTRAS.TEAM_REGISTERED, false);
    }
}
