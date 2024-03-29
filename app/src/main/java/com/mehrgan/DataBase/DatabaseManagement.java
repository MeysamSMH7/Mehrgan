package com.mehrgan.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mehrgan.DataBase.Structure.tb_BillsStructure;


public class DatabaseManagement extends SQLiteOpenHelper {

    public static final String databaseName = "Bills.db";
    public static final int databaseVersion = 1;

    public DatabaseManagement(Context context){
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tb_BillsStructure.createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
