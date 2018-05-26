package com.example.nimet.bestday.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nimet.bestday.data.BestdayContract.NotlarEntry;
import com.example.nimet.bestday.data.BestdayContract.KategoriEntry;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="bestday.db";
    private static final int DATABASE_VERSION=2;
    private static final String TABLE_KATEGORILER_CREATE=
            "CREATE TABLE " + KategoriEntry.TABLE_NAME + " ("+
                    KategoriEntry._ID + " INTEGER PRIMARY KEY, "+
                    KategoriEntry.COLUMN_KATEGORI + " TEXT)";

    private static final String TABLE_NOTLAR_CREATE=
            "CREATE TABLE " + NotlarEntry.TABLE_NAME + " ("+
                    NotlarEntry._ID + " INTEGER PRIMARY KEY, "+
                    NotlarEntry.COLUMN_NOT_ICERIK + " TEXT, " +
                    NotlarEntry.COLUMN_OLUSTURULMA_TARIHI + " TEXT DEFAULT CURRENT_TIMESTAMP, "+
                    NotlarEntry.COLUMN_BITIS_TARIHI +  " TEXT,"+
                    NotlarEntry.COLUMN_YAPILDI + " INTEGER," +
                    NotlarEntry.COLUMN_KATEGORI_ID + " INTEGER,"+
                    " FOREIGN KEY ( "+ NotlarEntry.COLUMN_KATEGORI_ID + ")" + " REFERENCES "+ KategoriEntry.TABLE_NAME +
                    "("+ KategoriEntry._ID + ") " + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_KATEGORILER_CREATE);
        db.execSQL(TABLE_NOTLAR_CREATE);

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + BestdayContract.KategoriEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ BestdayContract.NotlarEntry.TABLE_NAME);
        onCreate(db);

    }
}
