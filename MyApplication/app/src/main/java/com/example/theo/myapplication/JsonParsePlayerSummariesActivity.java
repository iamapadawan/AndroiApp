package com.example.theo.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class JsonParsePlayerSummariesActivity extends AppCompatActivity {

    private String TAG = JsonParsePlayerSummariesActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private String steamId;
    private ListView lv;
    Button btnGetPlayerSummaries;


    // URL to get steam login JSON
    private static String url;

    ArrayList<HashMap<String, String>> HashMapPlayerSummaries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        HashMapPlayerSummaries = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);
        new GetSteamId().execute();
        SharedPreferences lSharedPreferences = getSharedPreferences("steamId_prefs", 0);
        String m = lSharedPreferences.getString("SID", "");
        Toast.makeText(this, "SteamId: "+m, Toast.LENGTH_SHORT).show();
        url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=8A968A29B5EE8CB9E9BF3E4F4920B6C8&steamids="+m;
    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetSteamId extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(JsonParsePlayerSummariesActivity.this);
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
                    JSONObject response = jsonObj.getJSONObject("response");
                    JSONArray players = response.getJSONArray("players");

                    // looping through All Contacts
                    for (int i = 0; i < players.length(); i++) {
                        JSONObject lplayers = players.getJSONObject(i);

                        String id = "steamid: "+lplayers.getString("steamid");
                        String state = "communityvisibilitystate: "+lplayers.getString("communityvisibilitystate");
                        String profilestate = "profilestate: "+lplayers.getString("profilestate");
                        String personaname = "personaname: "+lplayers.getString("personaname");
                        String lastlogoff = "lastlogoff: "+lplayers.getString("lastlogoff");
                        String profileurl = "profileurl: "+lplayers.getString("profileurl");
                        String avatar = "avatar: "+lplayers.getString("avatar");
                        String avatarmedium = "avatarmedium: "+lplayers.getString("avatarmedium");
                        String avatarfull = "avatarfull: "+lplayers.getString("avatarfull");
                        String personastate = "personastate: "+lplayers.getString("personastate");
                        //String realname = "realname: "+lplayers.getString("realname");
                        String primaryclanid = "primaryclanid: "+lplayers.getString("primaryclanid");
                        String timecreated = "timecreated: "+lplayers.getString("timecreated");
                        String personastateflags = "personastateflags: "+lplayers.getString("personastateflags");
                        //String loccountrycode = "loccountrycode: "+lplayers.getString("loccountrycode");
                        //String locstatecode = "locstatecode: "+lplayers.getString("locstatecode");
                        //String loccityid="loccityid: "+lplayers.getString("loccityid");

                        // tmp hash map for single contact
                        HashMap<String, String> HashMapSteamLogin = new HashMap<>();

                        // adding each child node to HashMap key => value
                        HashMapSteamLogin.put("steamid", id);
                        HashMapSteamLogin.put("communityvisibilitystate", state);
                        HashMapSteamLogin.put("profilestate", profilestate);
                        HashMapSteamLogin.put("personaname", personaname);
                        HashMapSteamLogin.put("lastlogoff", lastlogoff);
                        HashMapSteamLogin.put("profileurl", profileurl);
                        HashMapSteamLogin.put("avatar", avatar);
                        HashMapSteamLogin.put("avatarmedium", avatarmedium);
                        HashMapSteamLogin.put("avatarfull", avatarfull);
                        HashMapSteamLogin.put("personastate", personastate);
                        //HashMapSteamLogin.put("realname", realname);
                        HashMapSteamLogin.put("primaryclanid", primaryclanid);
                        HashMapSteamLogin.put("timecreated", timecreated);
                        HashMapSteamLogin.put("personastateflags", personastateflags);
                        //HashMapSteamLogin.put("loccountrycode", loccountrycode);
                        //HashMapSteamLogin.put("locstatecode", locstatecode);
                        //HashMapSteamLogin.put("loccityid", loccityid);

                        // adding contact to contact list
                        HashMapPlayerSummaries.add(HashMapSteamLogin);
                    }
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
                    JsonParsePlayerSummariesActivity.this, HashMapPlayerSummaries,
                    R.layout.list_item, new String[]{"steamid","communityvisibilitystate","profilestate", "personaname", "lastlogoff", "profileurl", "avatar", "avatarmedium", "avatarfull", "personastate", "primaryclanid", "timecreated", "personastateflags"}, new int[]
                    {R.id.steamid, R.id.communityvisibilitystate, R.id.profilestate, R.id.personaname, R.id.lastlogoff,R.id.profileurl, R.id.avatar, R.id.avatarmedium, R.id.avatarfull, R.id.personastate, R.id.timecreated, R.id.personastateflags});

            lv.setAdapter(adapter);
        }

    }

}
