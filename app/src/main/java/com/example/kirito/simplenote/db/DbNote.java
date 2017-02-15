package com.example.kirito.simplenote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kirito.simplenote.entity.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirito on 2017.02.14.
 */

public class DbNote {
    private DbHelper helper;
    private SQLiteDatabase db;
    private static DbNote dbNote;
    private static final String TAG = "DbNote";

    public DbNote (Context context){
        helper = new DbHelper(context);
        db = helper.getWritableDatabase();
    }

    public static synchronized DbNote getInstance(Context context){
        if (dbNote == null){
            dbNote = new DbNote(context);
        }
        return dbNote;
    }

    public void saveNote(Item item){
        if (item != null){
            ContentValues values = new ContentValues();
            values.put(DbHelper.COLUMN_NOTE_TIME,item.getTime());
            values.put(DbHelper.COLUMN_NOTE_TITLE,item.getTitle());
            values.put(DbHelper.COLUMN_NOTE_ID,item.getId());
            db.insert(DbHelper.TABLE_NAME,null,values);
        }
    }

    public void saveAll(List<Item> itemList){
        for (Item item:itemList
             ) {
            saveNote(item);
        }
    }

    public void clearData(List<Item> itemList){
        for (Item item:itemList
                ) {
            deleteNote(item);
        }
    }

    public List<Item> loadNotes(){
        List<Item> items = new ArrayList<>();
        Cursor cursor = db.query(DbHelper.TABLE_NAME,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                Item item = new Item(cursor.getString(3),cursor.getString(2),cursor.getInt(1));
                items.add(item);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public void deleteNote(Item item){
        db.delete(DbHelper.TABLE_NAME,DbHelper.COLUMN_NOTE_ID + "= ?",new String[]{item.getId() + ""});
    }

    public void modifyNote(Item item){
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_NOTE_TIME,item.getTime());
        values.put(DbHelper.COLUMN_NOTE_TITLE,item.getTitle());
        values.put(DbHelper.COLUMN_NOTE_ID,item.getId());
        db.update(DbHelper.TABLE_NAME,values,DbHelper.COLUMN_NOTE_ID + "= ?",new String[]{item.getId() + ""});
    }
}
