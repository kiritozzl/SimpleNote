package com.example.kirito.simplenote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kirito on 2017.02.14.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "note.db";
    public static final String TABLE_NAME = "note";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE_TITLE = "note_title";
    public static final String COLUMN_NOTE_TIME = "note_time";
    public static final String COLUMN_NOTE_ID = "note_id";
    public static final int VERSION = 1;

    public static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_NAME
            +"( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_NOTE_ID + " INTEGER UNIQUE, "
            +COLUMN_NOTE_TIME + " TEXT, "
            +COLUMN_NOTE_TITLE + " TEXT );";

    public DbHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
