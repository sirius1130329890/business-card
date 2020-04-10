package com.example.myapp_x;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contactDb";
    public static final String TABLE_CONTACTS = "contacts";

    public static final String KEY_ID = "_id";
    public static final String KEY_PICTURE = "picture";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_NAME_PREFIX = "namePrefix";
    public static final String KEY_NAME_SUFFIX = "nameSuffix";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_WEBSITE = "website";
    public static final String KEY_FACEBOOK = "facebook";
    public static final String KEY_INSTAGRAM = "instagram";

    public DBHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID + " integer primary key," + KEY_PICTURE + " text," +
                KEY_FIRST_NAME + " text," + KEY_LAST_NAME + " text," + KEY_NAME_PREFIX + " text," +
                KEY_NAME_SUFFIX + " text," + KEY_NUMBER + " text," + KEY_EMAIL + " text," + KEY_WEBSITE +
                " text," + KEY_FACEBOOK + " text," + KEY_INSTAGRAM + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);

    }
}
