package com.example.theo.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonParseLoginActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private String steamId;
    private ListView lv;
    Button btnGetPlayerSummaries;


    // URL to get steam login JSON
    private static String url;

    ArrayList<HashMap<String, String>> steamLoginList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_parse_login);

        steamLoginList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);
        new GetSteamId().execute();
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        url = message;


    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetSteamId extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(JsonParseLoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONObject steamid = jsonObj.getJSONObject("response");

                        JSONObject lsteamid = steamid;

                        String id = lsteamid.getString("steamid");
                        String state = lsteamid.getString("success");

                        // tmp hash map for single contact
                        HashMap<String, String> HashMapSteamLogin = new HashMap<>();

                        // adding each child node to HashMap key => value
                        HashMapSteamLogin.put("steamid", id);
                        HashMapSteamLogin.put("success", state);

                        //Here define all sharedpreferences code with key and value
                        SharedPreferences prefs = getSharedPreferences("steamId_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putString("SID", id );
                        edit.commit();


                        // adding contact to contact list
                        steamLoginList.add(HashMapSteamLogin);
                    //}
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    JsonParseLoginActivity.this, steamLoginList,
                    R.layout.list_item_steam_login, new String[]{"steamid"}, new int[]{R.id.steamid});

            lv.setAdapter(adapter);
        }

    }

}
