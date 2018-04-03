package com.example.janus.stocktracker.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public final class PortfolioDBOpenHelper extends SQLiteOpenHelper {


    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "portfolio.db";

    public PortfolioDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE " +
                PortfolioDBContract.PortfolioEntry.TABLE_NAME + " (" +
                PortfolioDBContract.PortfolioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " TEXT UNIQUE NOT NULL" + " );";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + PortfolioDBContract.PortfolioEntry.TABLE_NAME);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                PortfolioDBContract.PortfolioEntry.TABLE_NAME + "'");

        // re-create database
        onCreate(db);
    }

}
