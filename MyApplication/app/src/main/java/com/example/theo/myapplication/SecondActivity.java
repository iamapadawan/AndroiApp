package com.example.theo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SecondActivity extends AppCompatActivity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        String[] prenoms = new String[]{
                "Théo", "Ahmed", "Clement", "JB", "Thibaut", "Ayoub",
        };
        mListView =  (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> lAdapter = new ArrayAdapter<String>(SecondActivity.this,
                android.R.layout.simple_list_item_1, prenoms);
        mListView.setAdapter(lAdapter);

    }

}
