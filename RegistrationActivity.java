package com.soubhagya.android.game_test;

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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by soubhagya on 27/1/17.
 */

public class RegistrationActivity extends AppCompatActivity{

    private SharedPreferences mSharedPreferences;
    private String mPlayerId;

    private String mTeamname;
    private String mPhone;

    private DatabaseReference mFirebaseReference;

    //UI components
    private EditText mTeamnameEditText;
    private EditText mPhoneEditText;
    private Button mRegisterButton;

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

        mTeamnameEditText = (EditText) findViewById(R.id.team_name_editText);
        mTeamnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTeamname = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPhoneEditText = (EditText) findViewById(R.id.phone_editText);
        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPhone = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTeamname != null && mPhone != null) {
                    teamInDatabase(mTeamname, mPhone);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter all details!",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void teamInDatabase(String teamname, final String phone) {
        DatabaseReference registeredTeams = mFirebaseReference
                                            .child(Constants.FIREBASE.REGISTERED_TEAMS);

        final String phoneTeam = phone + teamname;

        registeredTeams.child(phoneTeam)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(),
                                    "Team already exists!",
                                    Toast.LENGTH_SHORT)
                                    .show();

                            Intent intent = CreateTeamActivity
                                    .newIntent(RegistrationActivity.this,
                                            true,
                                            mTeamname,
                                            mPhone);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    "Creating Team!",
                                    Toast.LENGTH_SHORT)
                                    .show();

                            Intent intent = CreateTeamActivity
                                    .newIntent(RegistrationActivity.this,
                                            false,
                                            mTeamname,
                                            mPhone);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (databaseError != null) {
                            Log.d("REGISTRATION ACTIVITY", "Error occurred in database");
                            Toast.makeText(getApplicationContext(),
                                    "Database error occurred!",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
}
