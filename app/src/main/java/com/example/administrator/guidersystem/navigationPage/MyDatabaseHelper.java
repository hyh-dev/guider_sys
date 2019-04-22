package com.example.administrator.guidersystem.navigationPage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    private static final String CREATE_PLANT="create table plant(" +
            "id integer primary key autoincrement," +
            "number text,"+
            "music_num text,"+
            "name text,"+
            "introduction text)";
    private static final String CREATE_PLANT_ONLINE="create table plantOnline(" +
            "id integer primary key autoincrement," +
            "name text,"+
            "imageID text,"+
            "area text,"+
            "engName text)";
    public MyDatabaseHelper( Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PLANT);
        db.execSQL(CREATE_PLANT_ONLINE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists plant");
        db.execSQL("drop table if exists plantOnline");
        onCreate(db);
    }
}
