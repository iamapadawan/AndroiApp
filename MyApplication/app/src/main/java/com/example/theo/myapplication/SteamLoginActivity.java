package com.example.theo.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A login screen that offers login via Steam short Url.
 */
public class SteamLoginActivity extends AppCompatActivity {

    private String URL = "";

    EditText editTextSteamID;
    Button btnAddSteamID;
    SteamIdDataBaseAdapter SteamIdDataBaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steam_login);

        SteamIdDataBaseAdapter = new SteamIdDataBaseAdapter(this);
        SteamIdDataBaseAdapter = SteamIdDataBaseAdapter.open();
        editTextSteamID = (EditText) findViewById(R.id.TextSteamLogin);
        btnAddSteamID = (Button) findViewById(R.id.buttonAddYourSteamAccount);


        btnAddSteamID.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String steamID = editTextSteamID.getText().toString();
                URL="http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=8A968A29B5EE8CB9E9BF3E4F4920B6C8&vanityurl="+steamID;
                if (steamID.equals("")) {

                    Toast.makeText(getApplicationContext(),"Field Vaccant",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    Toast.makeText(getApplicationContext(),"Player id get success",Toast.LENGTH_LONG).show();
                    SteamIdDataBaseAdapter.insertEntry(steamID);
                    Intent lintent = new Intent(SteamLoginActivity.this,JsonParseLoginActivity.class);
                    lintent.putExtra("message",URL);
                    startActivity(lintent);
                    finish();
                }
            }
        });

    }



}