package com.example.theo.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ahmedsalem on 06/12/2016.
 */

public class SteamIdDataBaseAdapter {

    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    static final String DATABASE_CREATE = "create table " + "STEAM_LOGIN" + "( "
            + "ID" + " integer primary key autoincrement,"
            + "STEAMUSERNAME  text); ";
    public SQLiteDatabase db;
    private final Context context;
    private DataBaseHelper dbHelper;

    public SteamIdDataBaseAdapter(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public SteamIdDataBaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public void insertEntry(String SteamuserName) {
        ContentValues newValues = new ContentValues();
        newValues.put("STEAMUSERNAME", SteamuserName);
        db.insert("STEAM_LOGIN", null, newValues);
    }

    public int deleteEntry(String SteamUserName) {

        String where = "STEAMUSERNAME=?";
        int numberOFEntriesDeleted = db.delete("STEAM_LOGIN", where,
                new String[] { SteamUserName });
        return numberOFEntriesDeleted;
    }

    public String getSinlgeEntry(String SteamUserName) {
        Cursor cursor = db.query("STEAM_LOGIN", null, " STEAMUSERNAME=?",
                new String[] { SteamUserName }, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String steamusername = cursor.getString(cursor.getColumnIndex("STEAMUSERNAME"));
        cursor.close();
        return steamusername;
    }

    public void updateEntry(String steamUserName) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("STEAMUSERNAME", steamUserName);

        String where = "STEAMUSERNAME = ?";
        db.update("STEAM_LOGIN", updatedValues, where, new String[] { steamUserName });
    }
}
