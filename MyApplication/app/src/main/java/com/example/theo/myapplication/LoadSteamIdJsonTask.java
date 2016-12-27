package com.example.theo.myapplication;

import android.os.AsyncTask;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by ahmedsalem on 08/11/2016.
 */

public class LoadSteamIdJsonTask extends AsyncTask<String, Void, SteamIdReponse>{

    public LoadSteamIdJsonTask(View.OnClickListener listener) {

        //mListener = listener;
    }

    public interface Listener {

        void onLoaded(SteamID steamid);

        void onError();
    }

    private Listener mListener;

    @Override
    protected SteamIdReponse doInBackground(String... strings) {
        try {

            String stringResponse = loadJSON(strings[0]);
            Gson gson = new Gson();

            return gson.fromJson(stringResponse, SteamIdReponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onPostExecute(SteamIdReponse response) {

        if (response != null) {

            mListener.onLoaded(response.getSteamID());

        } else {

            mListener.onError();
        }
    }

    private String loadJSON(String jsonURL) throws IOException {

        URL url = new URL(jsonURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = in.readLine()) != null) {

            response.append(line);
        }

        in.close();
        return response.toString();
    }

}
